package com.codetosalvation.mongo.services.service;


public interface IndexService {
	public void createIndex(String collectionName, String indexFieldName);

	public void dropAllIndexes(String collectionName);
}
