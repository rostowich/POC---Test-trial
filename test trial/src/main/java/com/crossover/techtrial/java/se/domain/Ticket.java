package com.crossover.techtrial.java.se.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.crossover.techtrial.java.se.objects.Currency;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents the user tickets entity
 * @author 
 *
 */
@Entity
@Table(name="tickets")
public class Ticket implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	@Column(name = "ID", nullable = false)
	private Long id;
	
	@Column(name = "DEPARTURE")
	private String departure;
	
	@Column(name = "DESTINATION")
	private String destination;
	
	@Column(name = "AMOUNT")
	private Long amount;
	
	@Column(name = "CURRENCY")
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
	@Column(name = "NUMBER")
	private Integer number;
	
	@Column(name = "date")
	private Date date;
	
	/**
	 * This field will be used for search
	 */
	@Transient
	private Date beginDate;
	
	/**
	 * This field will be used for search
	 */
	@Transient
	private Date endDate;
	
	@ManyToOne
    @JoinColumn(name = "user_id")
	private User user;
	
	public Ticket() {
		super();
	}

	public Ticket(String departure, String destination, Long amount, Currency currency, Integer number) {
		super();
		this.departure = departure;
		this.destination = destination;
		this.amount = amount;
		this.currency = currency;
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeparture() {
		return departure;
	}

	public void setDeparture(String departure) {
		this.departure = departure;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
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

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@JsonIgnore
	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@JsonIgnore
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", departure=" + departure + ", destination=" + destination + ", amount=" + amount
				+ ", currency=" + currency + ", number=" + number + ", date=" + date +  "]";
	}
	
	
	
}
