package com.crossover.techtrial.java.se.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This Exception is thrown when the email is not sent
 * @author
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Error when sending the mail")
public class MailSendingException extends Exception{

private static final long serialVersionUID = 1L;
	
	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public MailSendingException() {
		super();
	}

	public MailSendingException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
}
