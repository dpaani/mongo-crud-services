package com.codetosalvation.mongo.services.web.resource;

import com.codetosalvation.mongo.services.exception.ApplicationException;
import com.codetosalvation.mongo.services.model.AppErroResponse;
import com.codetosalvation.mongo.services.util.ConverterUtil;
import com.codetosalvation.mongo.services.util.ExceptionUtil;
import com.codetosalvation.mongo.services.util.XmlUtil;

import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseResource {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected Response toApplicationErrorResponse(ApplicationException e, MediaType mediaType) {
		logger.error(e.getMessage(), e);
		final AppErroResponse appErroResponse = ExceptionUtil.toAppErrorResponse(e);
		if (isXMLRequest(mediaType.toString())) {

			return Response.ok(ConverterUtil.convertErrorObjectToXML(appErroResponse), mediaType)
					.status(e.getHttpResponseCode()).build();

		}
		return Response.ok(appErroResponse, mediaType).status(e.getHttpResponseCode()).build();
	}

	protected Response toInternalServerError(Exception e) {
		logger.error(e.getMessage(), e);
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).type(MediaType.TEXT_PLAIN).build();
	}

	protected Response toResponse(Object object, MediaType mediaType) {
		if (isXMLRequest(mediaType.toString())) {
			return Response.ok(ConverterUtil.convertObjectToXML(object), mediaType).build();
		}
		return Response.ok(object, mediaType).build();
	}

	@SuppressWarnings("rawtypes")
	protected Response toResponse(List list, MediaType mediaType) {
		if (isXMLRequest(mediaType.toString())) {
			return Response.ok(ConverterUtil.convertListOfObjectToXML(list), mediaType).build();
		}
		return Response.ok(list, mediaType).build();
	}

	protected String tranformRequest(String httpBody, String contentType) throws ApplicationException {
		if(isXMLRequest(contentType)) {
			if(!XmlUtil.isValidXML(httpBody)) {
				throw new ApplicationException("Invalid input XML",Status.BAD_REQUEST);
			}
			httpBody = ConverterUtil.convertXMLToJSON(httpBody);
		}
		return httpBody;
	}

	protected boolean isXMLRequest(String contentType) {
		return contentType.toLowerCase().contains("application/xml".toLowerCase());
	}

}
