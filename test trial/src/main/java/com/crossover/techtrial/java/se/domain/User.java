package com.crossover.techtrial.java.se.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.crossover.techtrial.java.se.objects.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents the user entity
 * @author
 *
 */
@Entity
@Table(name="users")
public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long userId;
	
	@Column(name = "EMAIL", unique = true, length=60)
	private String email;
	
	@Column(name = "PASSWORD_HASH")
	private String password;

	@Column(name = "ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="CODE")
	private Account account;
	
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY)
	private Collection<Ticket> tickets=new ArrayList<Ticket>();


	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}
	
	public User(String email, String password, Role role) {
		super();
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public User(String email, String password, Role role, Account account) {
		super();
		this.email = email;
		this.password = password;
		this.role = role;
		this.account = account;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@JsonIgnore
	public Collection<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(Collection<Ticket> tickets) {
		this.tickets = tickets;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", email=" + email + ", password=" + password + ", role=" + role
				+ ", account=" + account + "]";
	}
	
	

	
}
