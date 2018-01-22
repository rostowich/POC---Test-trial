package com.crossover.techtrial.java.se.objects;


public class PurchaseTicket {

	private int number;
	
	private AirlineOffer offer;
	
	private Long userId;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public AirlineOffer getOffer() {
		return offer;
	}

	public void setOffer(AirlineOffer offer) {
		this.offer = offer;
	}

	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public PurchaseTicket(int number, AirlineOffer offer, Long userId) {
		super();
		this.number = number;
		this.offer = offer;
		this.userId = userId;
	}

	public PurchaseTicket() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "PurchaseTicket [number=" + number + ", offer=" + offer + ", userId=" + userId + "]";
	}
	
	

}
