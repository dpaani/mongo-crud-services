package com.codetosalvation.mongo.services.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;

import com.codetosalvation.mongo.services.exception.ResourceNotFoundException;
import com.codetosalvation.mongo.services.util.Constants;
import com.codetosalvation.mongo.services.util.ConverterUtil;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public abstract class AbstractMongoBaseDao<T> {

	private final Logger logger = LoggerFactory.getLogger(AbstractMongoBaseDao.class);

	private final Class<T> entityClass;

	@Inject
	private final MongoOperations mongo = null;

	public AbstractMongoBaseDao(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	public T get(final String collectionName, final String objectId) {
		logger.debug("Getting document for object id: " + objectId + " from collection: " + collectionName);

		Assert.hasText(collectionName, "Collection Name must not be null!");
		Assert.hasText(objectId, "Object Id must not be null!");
		Assert.isTrue(isCollectionExists(collectionName), "Collection " + collectionName + " not found");

		final BasicDBObjectDocumentCallbackHandler responsehandler = new BasicDBObjectDocumentCallbackHandler();
		mongo.executeQuery(findByIdQuery(objectId), collectionName, responsehandler);

		final T response = responsehandler.getData();

		if (response == null) {
			throw new ResourceNotFoundException(objectId + " not found in the collection " + collectionName);
		}

		return response;
	}

	public List<T> search(String collectionName, String queryParams, String fieldParams, String sortParams,
			Integer skip, Integer limit) {

		Assert.hasText(collectionName, "Collection Name must not be null");
		Assert.hasText(queryParams, "Query Params must not be null");
		Assert.isTrue(isCollectionExists(collectionName), "Collection " + collectionName + " not found");

		final BasicQuery query = buildSearchQuery(queryParams, fieldParams, sortParams, skip, limit);
		logger.debug("Search query -->" + query.toString());
		return mongo.find(query, entityClass, collectionName);
	}

	private BasicQuery buildSearchQuery(String queryParams, String fieldParams, String sortParams,
			Integer skip, Integer limit) {

		final DBObject queryObject = ConverterUtil.toDBObject(queryParams);
		final DBObject fieldsObject = ConverterUtil.toDBObject(fieldParams);
		final DBObject sortObject = ConverterUtil.toDBObject(sortParams);

		BasicQuery query = null;
		if (StringUtils.isNotEmpty(fieldParams)) {
			query = new BasicQuery(queryObject, fieldsObject);
		} else {
			query = new BasicQuery(queryObject);
		}

		if (StringUtils.isNotEmpty(sortParams)) {
			query.setSortObject(sortObject);
		}

		if (skip != null) {
			query.skip(skip);
		}

		if (limit != null) {
			query.limit(limit);
		}

		return query;
	}

	@Profiled(logFailuresSeparately = true, message = "create-dao")
	public void insert(final String collectionName, DBObject dbObject) {
		Assert.hasText(collectionName, "Collection Name must not be null!");
		Assert.isTrue(isCollectionExists(collectionName), "Collection " + collectionName + " not found");
		this.mongo.insert(dbObject, collectionName);
	}

	@Profiled(logFailuresSeparately = true, message = "create-dao")
	public String insert(final String collectionName, final String id, final DBObject doc) {
		Assert.hasText(collectionName, "Collection Name must not be null!");
		Assert.hasText(id, "Object Id must not be null!");
		Assert.isTrue(isCollectionExists(collectionName), "Collection " + collectionName + " not found");

		this.mongo.insert(doc, collectionName);
		return id;
	}

	@Profiled(logFailuresSeparately = true, message = "save-dao")
	public T save(final String collectionName, final T doc) {
		this.mongo.save(doc, collectionName);
		return doc;
	}

	@Profiled(logFailuresSeparately = true, message = "save-dao")
	public void save(final String collectionName, final DBObject doc) {
		Assert.isTrue(isCollectionExists(collectionName), "Collection " + collectionName + " not found");
		this.mongo.save(doc, collectionName);
	}

	@Profiled(logFailuresSeparately = true, message = "deleteAll-dao")
	public void deleteAll(final String collectionName) {
		logger.info("DELETING ALL DOCUMENTS FROM COLLECTION:" + collectionName);
		this.mongo.remove(new BasicQuery("{}"), collectionName);
	}

	@Profiled(logFailuresSeparately = true, message = "doesDocumentExist-dao")
	public boolean doesDocumentExist(final String collectionName, String objectId) {
		return get(collectionName, objectId) != null;
	}

	private Query findByIdQuery(String objectId) {
		final DBObject queryObject = getQueryObject(objectId, null);

		final DBObject fieldsObject = new BasicDBObject();
		// fieldsObject.put("_id", 1);//calculatorInput

		final Query query = new BasicQuery(queryObject, fieldsObject);

		query.limit(1);

		return query;
	}

	@Profiled(logFailuresSeparately = true, message = "isCollectionExists-dao")
	public boolean isCollectionExists(String collectionName) {
		return this.mongo.collectionExists(collectionName);
	}

	private DBObject getQueryObject(final String id, final Criteria filterCritera) {
		final DBObject queryObject = new BasicDBObject();
		if (id != null) {
			queryObject.put(Constants.ID_FIELD, id);
		}

		if (filterCritera != null) {
			final DBObject filters = filterCritera.getCriteriaObject();
			queryObject.putAll(filters);
		}

		return queryObject;

	}

	@Profiled(logFailuresSeparately = true, message = "delete-dao")
	public void delete(String collectionName, String id) {
		Assert.notNull(id, "Object id must be not-null");
		Assert.isTrue(isCollectionExists(collectionName), "Collection " + collectionName + " not found");
		this.mongo.remove(findByIdQuery(id), collectionName);
	}

	private class BasicDBObjectDocumentCallbackHandler implements DocumentCallbackHandler {

		T response;

		@SuppressWarnings("unchecked")
		@Override
		public void processDocument(DBObject arg0) throws MongoException, DataAccessException {
			if (entityClass.isAssignableFrom(arg0.getClass())) {
				this.response = (T) arg0;
			}
		}

		public T getData() {
			return response;
		}
	}
}
