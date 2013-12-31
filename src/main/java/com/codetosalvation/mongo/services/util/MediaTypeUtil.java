package com.codetosalvation.mongo.services.util;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;


public final class MediaTypeUtil {

	private MediaTypeUtil() {}

	public static MediaType toMediaType(String contentType) {
		if(contentType == null)
			return MediaType.APPLICATION_JSON_TYPE;

		if(StringUtils.containsIgnoreCase(contentType,"xml")) {
			return MediaType.APPLICATION_XML_TYPE;
		}
		// default
		return MediaType.APPLICATION_JSON_TYPE;
	}
}
