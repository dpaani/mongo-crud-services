package com.codetosalvation.mongo.services.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import com.codetosalvation.mongo.services.dao.GenericCrudDao;
import com.codetosalvation.mongo.services.exception.ApplicationException;
import com.codetosalvation.mongo.services.exception.ResourceNotFoundException;
import com.codetosalvation.mongo.services.service.GenericCrudService;
import com.codetosalvation.mongo.services.util.Constants;
import com.codetosalvation.mongo.services.util.ConverterUtil;
import com.codetosalvation.mongo.services.util.DateUtil;

import java.util.List;

import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class GenericCrudServiceImpl<T> implements GenericCrudService<T> {

	@Autowired
	@Qualifier("genericCurdDao")
	GenericCrudDao<T> genericCrudDao;

	@Override
	public T getDcoument(final String collectionName, final String objectId) throws ApplicationException {
		try {
			return genericCrudDao.get(collectionName, objectId);
		} catch (final ResourceNotFoundException e) {
			throw new ApplicationException(e.getMessage(), Status.NOT_FOUND);
		} catch (final IllegalArgumentException e) {
			throw new ApplicationException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	@Override
	public List<T> search(String collectionName, String queryParams, String fieldParams, String sortParams,
			Integer skip, Integer limit) throws ApplicationException {
		try {
			return genericCrudDao.search(collectionName, queryParams, fieldParams, sortParams, skip, limit);
		} catch (final IllegalArgumentException e) {
			throw new ApplicationException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	@Override
	public T createDocument(String collectionName, String id, String postBody, String rootElement)
			throws ApplicationException {
		try {
			BasicDBObject document = (BasicDBObject) ConverterUtil.toDBObject(postBody);

			// to support empty document input
			if (document == null) {
				document = new com.mongodb.BasicDBObject();
			}

			String docId = id;
			if (StringUtils.isEmpty(docId)) {
				docId = createDocumentId();
			}
			document.put(Constants.ID_FIELD, docId);

			final DBObject updatedRootElementInDoc = (BasicDBObject) document.get(rootElement);
			if (updatedRootElementInDoc != null) {
				// set audit data
				appendAuditData(updatedRootElementInDoc);

				document.remove(rootElement);
				document.append(rootElement, updatedRootElementInDoc);
			}

			return genericCrudDao.create(collectionName, document);

		} catch (final org.springframework.dao.DuplicateKeyException e) {
			throw new ApplicationException(e.getMostSpecificCause().getMessage(), Status.BAD_REQUEST);
		} catch (final IllegalArgumentException e) {
			throw new ApplicationException(e.getMessage(), Status.BAD_REQUEST);
		}
	}

	private void appendAuditData(DBObject dbObject) {

		dbObject.put(Constants.createdDate, DateUtil.currentDateTimeAsString());
		dbObject.put("modDate", null);

		if (!dbObject.containsField(Constants.createdByApp)) {
			dbObject.put(Constants.createdByApp, null);
		}

		if (!dbObject.containsField(Constants.createdByUser)) {
			dbObject.put(Constants.createdByUser, null);
		}

		if (!dbObject.containsField(Constants.createTrxId)) {
			dbObject.put(Constants.createTrxId, null);
		}

	}

	private String createDocumentId() {
		final String docId = new ObjectId().toString();
		return docId;
	}

	@Override
	public T updateDocument(String collectionName, String postBody, String rootElement)
			throws ApplicationException {
		try {
			final BasicDBObject document = (BasicDBObject) ConverterUtil.toDBObject(postBody);
			Assert.notNull(document, "Input document must be not null");

			final String docId = document.getString("_id");
			Assert.hasText(docId, "Input document does not contain object id");

			final BasicDBObject oldDocument = (BasicDBObject) getDcoument(collectionName, docId);

			final BasicDBObject updatedRootElementInDoc = (BasicDBObject) document.get(rootElement);
			if (updatedRootElementInDoc != null) {
				appendAuditData(updatedRootElementInDoc,oldDocument);
			}

			return genericCrudDao.update(collectionName, document);
		} catch (final org.springframework.dao.DataAccessException e) {
			throw new ApplicationException(e.getMostSpecificCause().getMessage(), Status.BAD_REQUEST);
		} catch (final IllegalArgumentException e) {
			throw new ApplicationException(e.getMessage(), Status.BAD_REQUEST);
		} catch (final ResourceNotFoundException e) {
			throw new ApplicationException(e.getMessage(), Status.NOT_FOUND);
		}
	}

	private void appendAuditData(BasicDBObject updatedRootElementInDoc, BasicDBObject oldDocument) {
		updatedRootElementInDoc.put(Constants.modifiedDate, DateUtil.currentDateTimeAsString());
		updatedRootElementInDoc.put(Constants.createdDate,
				oldDocument.getString(Constants.createdDate));
		updatedRootElementInDoc.remove(Constants.createdByApp);
		updatedRootElementInDoc.put(Constants.createdByApp,
				oldDocument.getString(Constants.createdByApp));
		updatedRootElementInDoc.remove(Constants.createdByUser);
		updatedRootElementInDoc.put(Constants.createdByUser,
				oldDocument.getString(Constants.createdByUser));
		updatedRootElementInDoc.remove(Constants.createTrxId);
		updatedRootElementInDoc.put(Constants.createTrxId,
				oldDocument.getString(Constants.createTrxId));
	}

	@Override
	public void deleteDocument(String collectionName, String id) throws ApplicationException {
		try {
			genericCrudDao.delete(collectionName, id);
		} catch (final org.springframework.dao.DataAccessException e) {
			throw new ApplicationException(e.getMostSpecificCause().getMessage(), Status.BAD_REQUEST);
		} catch (final IllegalArgumentException e) {
			throw new ApplicationException(e.getMessage(), Status.BAD_REQUEST);
		} catch (final ResourceNotFoundException e) {
			throw new ApplicationException(e.getMessage(), Status.NOT_FOUND);
		}
	}

}
