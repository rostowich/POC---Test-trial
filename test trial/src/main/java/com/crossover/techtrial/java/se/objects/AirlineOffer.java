package com.crossover.techtrial.java.se.objects;

public class AirlineOffer {

	private Balance price;
	
	private AirlineRoute route;

	public Balance getPrice() {
		return price;
	}

	public void setPrice(Balance price) {
		this.price = price;
	}

	public AirlineRoute getRoute() {
		return route;
	}

	public void setRoute(AirlineRoute route) {
		this.route = route;
	}

	public AirlineOffer(Balance price, AirlineRoute route) {
		super();
		this.price = price;
		this.route = route;
	}

	@Override
	public String toString() {
		return "AirlineOffer [price=" + price + ", route=" + route + "]";
	}

	public AirlineOffer() {
		super();
	}

}
