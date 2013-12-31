package com.codetosalvation.mongo.services.util;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConverterUtil {
	private final static Logger logger = LoggerFactory.getLogger(ConverterUtil.class);
	public static final String DELIMITER_ELEMENT = " ,-;:";

	private ConverterUtil() {}

	public static String convertErrorObjectToXML(Object object) {
		final StringBuilder fault = new StringBuilder();
		fault.append(Constants.FAULT_START);
		fault.append(ConverterUtil.convertJSONToXML(JSONUtil.serialize(object)));
		fault.append(Constants.FAULT_END);
		return fault.toString();
	}

	public static String convertObjectToXML(Object object) {
		final StringBuilder xml = new StringBuilder();
		xml.append(Constants.DOCUMENT_START);
		xml.append(ConverterUtil.convertJSONToXML(JSONUtil.serialize(object)));
		xml.append(Constants.DOCUMENT_END);
		return xml.toString();
	}

	public static String convertListOfObjectToXML(@SuppressWarnings("rawtypes") List list) {
		final StringBuilder xmlResponseBuilder = new StringBuilder();
		xmlResponseBuilder.append(Constants.DOCUMENTS_START);
		for (final Object object : list) {
			xmlResponseBuilder.append(convertObjectToXML(object));
		}
		xmlResponseBuilder.append(Constants.DOCUMENTS_END);
		return xmlResponseBuilder.toString();
	}


	public static String convertJSONToXML(String json) {
		String xml = "";
		try {
			final JSONObject jsonObj = new JSONObject(json);
			xml = XML.toString(jsonObj);
		} catch (final JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return xml;
	}

	public static String convertXMLToJSON(String xml) {
		return ConverterUtil.convertXMLToJSON(xml, null);
	}

	public static String convertXMLToJSON(String xml, String arrayElements) {
		String result = "";
		try {
			final JSONObject json = XML.toJSONObject(xml);
			result = json.toString();
		} catch (final JSONException e) {
			logger.error(e.getMessage(), e);
		}
		return result;
	}

	public static DBObject toDBObject(String jsonString) {
		try {
			return (DBObject) JSON.parse(jsonString);
		} catch (final Exception e) {
			throw new IllegalArgumentException("Invalid JSON: " + e.getMessage());
		}
	}

}
