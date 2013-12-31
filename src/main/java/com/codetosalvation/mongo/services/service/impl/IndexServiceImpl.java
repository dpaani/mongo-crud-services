package com.codetosalvation.mongo.services.service.impl;

import com.codetosalvation.mongo.services.service.IndexService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class IndexServiceImpl implements IndexService {

	private final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

	@Autowired
	private final MongoOperations mongo = null;

	public void createIndex(String collectionName, String indexFieldName) {
		Assert.isTrue(mongo.collectionExists(collectionName), "Collection " + collectionName + " not found");
		logger.info("Creating index on collection :"+collectionName+" for field name: "+indexFieldName);
		mongo.indexOps(collectionName).ensureIndex(new Index().on(indexFieldName, Order.ASCENDING));
		logger.info("Index created successfully");
	}

	@Override
	public void dropAllIndexes(String collectionName) {
		Assert.isTrue(mongo.collectionExists(collectionName), "Collection " + collectionName + " not found");
		logger.info("Dropping index on collection :"+collectionName);
		mongo.indexOps(collectionName).dropAllIndexes();
		logger.info("All Indexes dropped successfully");


	}
}
