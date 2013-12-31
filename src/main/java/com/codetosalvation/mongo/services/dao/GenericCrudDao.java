package com.codetosalvation.mongo.services.dao;

import com.mongodb.DBObject;

import java.util.List;

public interface GenericCrudDao<T> {

	public T get(final String collectionName, final String objectId);

	public T get(final String collectionName, final DBObject dbObject);

	public List<T> search(String collectionName, String queryParams, String fieldParams, String sortParams,
			Integer skip, Integer limit);

	public T create(String collectionName, DBObject dbObject);

	public T update(String collectionName, DBObject dbObject);

	public void delete(String collectionName, String id);
}
