package com.crossover.techtrial.java.se.objects;

/*
 * This class is used to handle exception in the REST controller
 */
public class MessageDTO {

	private String message;
	
	private MessageType type;

	public MessageDTO(String message, MessageType type) {
		super();
		this.message = message;
		this.type = type;
	}

	public MessageDTO() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

}
