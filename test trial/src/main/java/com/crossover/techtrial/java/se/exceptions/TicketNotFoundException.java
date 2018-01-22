package com.crossover.techtrial.java.se.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception is thrown when the user ticket is not found
 * @author
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Ticket not found")
public class TicketNotFoundException extends Exception {

private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public TicketNotFoundException() {
		super();
	}

	public TicketNotFoundException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
}
