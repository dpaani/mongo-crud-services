package com.codetosalvation.mongo.services.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestClientException extends RuntimeException {

	BindingResult bindingResult;
	public BadRequestClientException() {
		super();
	}

	public BadRequestClientException(String message) {
		super(message);
	}

	public BadRequestClientException(String message, BindingResult bindingResult) {
		super(message);
		this.bindingResult = bindingResult;
	}


	public BindingResult getBindingResult() {
		return bindingResult;
	}



}
