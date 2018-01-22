package com.crossover.techtrial.java.se.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception class is used to intercept Exception from the external system
 * @author
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Error when sending request to the external system")
public class AirlineApiException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public AirlineApiException() {
		super();
	}

	public AirlineApiException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
}
