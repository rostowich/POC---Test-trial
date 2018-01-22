package com.crossover.techtrial.java.se.objects;

public class ErrorApiResponse {

	
	private String message;

	public ErrorApiResponse(String message) {
		super();
		this.message = message;
	}

	public ErrorApiResponse() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ErrorApiResponse [message=" + message + "]";
	}
	
	
	
	
}
