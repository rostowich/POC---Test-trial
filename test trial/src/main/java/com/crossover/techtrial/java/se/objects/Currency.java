package com.crossover.techtrial.java.se.objects;

public enum Currency {
	USD("USD"),   
    EUR("EUR"),
    AED("AED"),   
    AUD("AUD");
	
	private String currency;

	private Currency(String currency) {
		this.currency = currency;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
