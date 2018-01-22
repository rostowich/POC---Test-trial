package com.crossover.techtrial.java.se.objects;

public class EmailMessage {

	private String email;
	
	private Long ticketId;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	@Override
	public String toString() {
		return "EmailMessage [email=" + email + ", ticketId=" + ticketId + "]";
	}

	public EmailMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmailMessage(String email, Long ticketId) {
		super();
		this.email = email;
		this.ticketId = ticketId;
	}

	
	
}
