package com.crossover.techtrial.java.se.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse {

	/**
	 * The account balance
	 */
    private Balance balance;
    
    /**
     * The account ID
     */
    private String id;

    public AccountResponse() {
    }

	public AccountResponse(Balance balance, String id) {
		super();
		this.balance = balance;
		this.id = id;
	}

	@Override
	public String toString() {
		return "Account [balance=" + balance + ", id=" + id + "]";
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
    
}
