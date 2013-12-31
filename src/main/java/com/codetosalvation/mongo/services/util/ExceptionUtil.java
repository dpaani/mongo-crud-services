package com.codetosalvation.mongo.services.util;

import com.codetosalvation.mongo.services.exception.ApplicationException;
import com.codetosalvation.mongo.services.model.AppErroResponse;
import com.codetosalvation.mongo.services.model.ErrorResponse;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ExceptionUtil {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

	private ExceptionUtil() {
	}

	public static AppErroResponse toAppErrorResponse(ApplicationException e) {
		final AppErroResponse appErrorResponse = new AppErroResponse();
		final ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setMessage(e.getMessage());
		errorResponse.setErrorReferenceId(UUID.randomUUID().toString());
		errorResponse.setType(e.getHttpResponseCode().name());
		appErrorResponse.addFaultMessage(errorResponse);
		ExceptionUtil.logger.error("Error message: " + errorResponse.getMessage() + ", Error code: "
				+ errorResponse.getErrorReferenceId(), e);
		return appErrorResponse;
	}
}
