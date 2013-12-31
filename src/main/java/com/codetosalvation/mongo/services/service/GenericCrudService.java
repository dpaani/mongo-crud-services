package com.codetosalvation.mongo.services.service;

import com.codetosalvation.mongo.services.exception.ApplicationException;

import java.util.List;

public interface GenericCrudService<T> {

	public T getDcoument(final String collectionName, final String objectId) throws ApplicationException;

	public List<T> search(String collectionName, String queryParams, String fieldParams, String sortParams,
			Integer skip, Integer limit) throws ApplicationException;

	public T createDocument(String collectionName, String id, String postBody, String rootElement) throws ApplicationException;

	public T updateDocument(String collectionName, String httpBody, String rootElement) throws ApplicationException;

	public void deleteDocument(String collectionName, String id)throws ApplicationException ;
}
