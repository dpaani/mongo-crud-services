package com.codetosalvation.mongo.services.exception;

import javax.ws.rs.core.Response.Status;


public class ApplicationException extends Exception {

	private static final long serialVersionUID = 1L;

	private Status httpStatus;

	private String errorCode = ErrorCodes.NOT_SET;

	public ApplicationException(Status httpResponseCode) {
		super();
		this.httpStatus = httpResponseCode;
	}

	public ApplicationException(String message, Status httpResponseCode, String errorCode) {
		super(message);
		this.httpStatus = httpResponseCode;
		this.errorCode = errorCode;
	}

	public ApplicationException(String message, Throwable cause, Status httpResponseCode) {
		super(message, cause);
		this.httpStatus = httpResponseCode;
	}

	public ApplicationException(String message, Status httpResponseCode) {
		super(message);
		this.httpStatus = httpResponseCode;
	}

	public ApplicationException(Throwable cause, Status httpResponseCode) {
		super(cause);
		this.httpStatus = httpResponseCode;
	}

	public Status getHttpResponseCode() {
		return httpStatus;
	}

	public void setHttpResponseCode(Status httpResponseCode) {
		this.httpStatus = httpResponseCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
