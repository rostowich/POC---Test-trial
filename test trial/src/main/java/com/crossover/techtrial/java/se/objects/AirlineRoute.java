package com.crossover.techtrial.java.se.objects;

public class AirlineRoute {
	
	private String from;
	
	private String to;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public String toString() {
		return "AirlineRoute [from=" + from + ", to=" + to + "]";
	}

	public AirlineRoute(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}

	public AirlineRoute() {
		super();
	}

	
}
