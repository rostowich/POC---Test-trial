package com.crossover.techtrial.java.se.objects;

public class ByeTicketResponse {

	private AirlineOffer details;
		
	private int amount;

	public AirlineOffer getDetails() {
		return details;
	}

	public void setDetails(AirlineOffer details) {
		this.details = details;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public ByeTicketResponse(AirlineOffer details, int amount) {
		super();
		this.details = details;
		this.amount = amount;
	}

	public ByeTicketResponse() {
		super();
	}

}
