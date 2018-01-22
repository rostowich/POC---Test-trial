package com.crossover.techtrial.java.se.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception is thrown when the user account does not have enough money
 * @author
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Not enough amount in the account")
public class NotEnoughAmountInAccountException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public NotEnoughAmountInAccountException() {
		super();
	}

	public NotEnoughAmountInAccountException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
}
