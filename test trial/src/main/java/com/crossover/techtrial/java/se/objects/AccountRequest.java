package com.crossover.techtrial.java.se.objects;

public class AccountRequest {

	/**
	 * The ID of the applicant
	 */
	private String applicantId;

	/**
	 * the currency of the account
	 */
	private Currency currency;
	
	public AccountRequest() {
		
	}

	public AccountRequest(String applicantId, Currency currency) {
		super();
		this.applicantId = applicantId;
		this.currency = currency;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "AccountRequest [applicantId=" + applicantId + ", currency=" + currency + "]";
	}

}
