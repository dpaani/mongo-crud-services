package com.codetosalvation.mongo.services.util;

import com.mongodb.BasicDBObject;
import com.sun.jersey.core.provider.AbstractMessageReaderWriterProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
@Provider
@Produces({ MediaType.APPLICATION_XML })
public class BasicDBObjectResponseProvider extends AbstractMessageReaderWriterProvider<BasicDBObject> {

	public final Log logger = LogFactory.getLog(BasicDBObjectResponseProvider.class);

	@Override
	public boolean isReadable(Class<?> arg0, Type arg1, Annotation[] arg2, MediaType arg3) {
		return false;
	}

	@Override
	public BasicDBObject readFrom(Class<BasicDBObject> arg0, Type arg1, Annotation[] arg2,
			MediaType mediaType, MultivaluedMap<String, String> arg4, InputStream entityStream)
			throws IOException, WebApplicationException {
		return null;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type arg1, Annotation[] arg2, MediaType arg3) {
		return type == BasicDBObject.class;
	}

	@Override
	public void writeTo(BasicDBObject response, Class<?> arg1, Type arg2, Annotation[] arg3,
			MediaType mediaType, MultivaluedMap<String, Object> arg5, OutputStream entityStream)
			throws IOException, WebApplicationException {
		try {
			AbstractMessageReaderWriterProvider
					.writeToAsString(
							Constants.DOCUMENT_START
									+ (ConverterUtil.convertJSONToXML(JSONUtil.serialize(response)) + Constants.DOCUMENT_END),
							entityStream, mediaType);
		} catch (final Exception e) {
			logger.error("Unable to convert response object to string");
		}
	}
}
