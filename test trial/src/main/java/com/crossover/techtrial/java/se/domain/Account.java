package com.crossover.techtrial.java.se.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.crossover.techtrial.java.se.objects.Currency;

/**
 * This class represents the user account entity
 * @author
 *
 */
@Entity
@Table(name="accounts")
public class Account implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="CODE",length=20)
	private String id;
	
	@Column(name="BALANCE")
	private Long balance;
	
	@Column(name="CURRENCY", length=5)
	@Enumerated(EnumType.STRING)
	private Currency currency;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Account(String id, Long balance, Currency currency) {
		super();
		this.id = id;
		this.balance = balance;
		this.currency = currency;
	}

	public Account() {
		super();
	}

	@Override
	public String toString() {
		return "Account [code=" + id + ", balance=" + balance + ", currency=" + currency + "]";
	}
	
	

}
