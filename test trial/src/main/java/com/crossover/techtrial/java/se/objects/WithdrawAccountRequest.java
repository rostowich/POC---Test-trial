package com.crossover.techtrial.java.se.objects;

public class WithdrawAccountRequest {

	/**
	 * ID of the account to be credited
	 */
	private String accountId;
	
	/**
	 * The money to withdraw
	 */
	private Balance monetaryAmount;

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Balance getMonetaryAmount() {
		return monetaryAmount;
	}

	@Override
	public String toString() {
		return "WithdrawAccountRequest [accountId=" + accountId + ", monetaryAmount=" + monetaryAmount + "]";
	}

	public WithdrawAccountRequest() {
		super();
	}

	public WithdrawAccountRequest(String accountId, Balance monetaryAmount) {
		super();
		this.accountId = accountId;
		this.monetaryAmount = monetaryAmount;
	}

	public void setMonetaryAmount(Balance monetaryAmount) {
		this.monetaryAmount = monetaryAmount;
	}
	
	
}
