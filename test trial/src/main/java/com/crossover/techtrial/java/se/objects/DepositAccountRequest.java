package com.crossover.techtrial.java.se.objects;

public class DepositAccountRequest {
	
	/**
	 * ID of the applicant
	 */
	private String applicantId;
	
	/**
	 * ID of the account to be credited
	 */
	private String accountId;
	
	private Balance monetaryAmount;

	
	
	public DepositAccountRequest() {
		super();
	}

	@Override
	public String toString() {
		return "DepositAccount [applicantId=" + applicantId + ", accountId=" + accountId + ", monetaryAmount="
				+ monetaryAmount + "]";
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public Balance getMonetaryAmount() {
		return monetaryAmount;
	}

	public void setMonetaryAmount(Balance monetaryAmount) {
		this.monetaryAmount = monetaryAmount;
	}

	public DepositAccountRequest(String applicantId, String accountId, Balance monetaryAmount) {
		super();
		this.applicantId = applicantId;
		this.accountId = accountId;
		this.monetaryAmount = monetaryAmount;
	}

}
