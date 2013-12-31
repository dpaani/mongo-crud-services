package com.codetosalvation.mongo.services.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {

	public InternalServerErrorException() {
		super();
	}

	public InternalServerErrorException(String message) {
		super(message);
	}

	public InternalServerErrorException(Exception e) {
		super(e);
	}

	public InternalServerErrorException(String message, Exception e) {
		super(message, e);
	}

}
