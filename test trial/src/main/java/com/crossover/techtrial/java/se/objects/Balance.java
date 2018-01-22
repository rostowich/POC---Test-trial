package com.crossover.techtrial.java.se.objects;

public class Balance {

	/**
	 * The balance amount
	 */
	private Long amount;
	
	/**
	 * The balance currency
	 */
	private Currency currency;

	public Balance() {
		
	}
	
	public Balance(Long amount, Currency currency) {
		super();
		this.amount = amount;
		this.currency = currency;
	}

	
	@Override
	public String toString() {
		return "Balance [amount=" + amount + ", currency=" + currency + "]";
	}


	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

}
