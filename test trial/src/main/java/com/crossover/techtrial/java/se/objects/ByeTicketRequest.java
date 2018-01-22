package com.crossover.techtrial.java.se.objects;

public class ByeTicketRequest {
	
	private String applicantId;
	
	private Integer amount;
	
	private String accountId;
	
	private AirlineRoute route;

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public AirlineRoute getRoute() {
		return route;
	}

	public void setRoute(AirlineRoute route) {
		this.route = route;
	}

	

	public ByeTicketRequest(String applicantId, Integer amount, String accountId, AirlineRoute route) {
		super();
		this.applicantId = applicantId;
		this.amount = amount;
		this.accountId = accountId;
		this.route = route;
	}

	public ByeTicketRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ByeTicketRequest [applicantId=" + applicantId + ", amount=" + amount + ", accountId=" + accountId
				+ ", route=" + route + "]";
	}

}
