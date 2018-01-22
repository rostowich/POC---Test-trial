package com.crossover.techtrial.java.se.objects;

public class ExchangeRequest {

	private String applicantId;
	
	private Currency targetCurrency;
	
	private Balance monetaryAmount;

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public Currency getTargetCurrency() {
		return targetCurrency;
	}

	public void setTargetCurrency(Currency targetCurrency) {
		this.targetCurrency = targetCurrency;
	}

	public Balance getMonetaryAmount() {
		return monetaryAmount;
	}

	public void setMonetaryAmount(Balance monetaryAmount) {
		this.monetaryAmount = monetaryAmount;
	}

	public ExchangeRequest(String applicantId, Currency targetCurrency, Balance monetaryAmount) {
		super();
		this.applicantId = applicantId;
		this.targetCurrency = targetCurrency;
		this.monetaryAmount = monetaryAmount;
	}

	public ExchangeRequest() {
		super();
	}

	@Override
	public String toString() {
		return "ExchangeRequest [applicantId=" + applicantId + ", targetCurrency=" + targetCurrency
				+ ", monetaryAmount=" + monetaryAmount + "]";
	}
	
	
}
