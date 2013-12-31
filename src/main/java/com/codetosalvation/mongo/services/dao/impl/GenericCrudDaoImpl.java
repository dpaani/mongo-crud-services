package com.codetosalvation.mongo.services.dao.impl;

import com.mongodb.DBObject;

import com.codetosalvation.mongo.services.dao.GenericCrudDao;
import com.codetosalvation.mongo.services.util.Constants;

import java.util.List;

import org.springframework.util.Assert;

public class GenericCrudDaoImpl<T> extends AbstractMongoBaseDao<T> implements GenericCrudDao<T> {

	public GenericCrudDaoImpl(Class<T> entityClass) {
		super(entityClass);
	}

	public T get(final String collectionName, final DBObject dbObject) {
		final String _id = (String) dbObject.get(Constants.ID_FIELD);
		Assert.hasText(_id, "Object Id must not be null!");
		return super.get(collectionName, _id);
	}

	@Override
	public T get(final String collectionName, final String objectId) {
		return super.get(collectionName, objectId);
	}

	@Override
	public List<T> search(String collectionName, String queryParams, String fieldParams, String sortParams,
			Integer skip, Integer limit) {

		return super.search(collectionName, queryParams, fieldParams, sortParams, skip, limit);
	}

	@Override
	public T create(String collectionName, DBObject dbObject) {
		// create document
		super.insert(collectionName, dbObject);
		return get(collectionName, dbObject);
	}

	@Override
	public T update(String collectionName, DBObject dbObject) {
		// update document
		super.save(collectionName, dbObject);
		return get(collectionName, dbObject);
	}

	@Override
	public void delete(String collectionName, String id) {
		super.doesDocumentExist(collectionName, id);
		super.delete(collectionName, id);
	}

}
