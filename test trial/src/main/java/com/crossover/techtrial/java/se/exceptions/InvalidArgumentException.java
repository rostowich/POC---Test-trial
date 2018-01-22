package com.crossover.techtrial.java.se.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception is thrown when the user input invalid parameters for a request
 * @author
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="The argument sent is not valid")
public class InvalidArgumentException extends Exception{

private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public InvalidArgumentException() {
		super();
	}

	public InvalidArgumentException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
}
