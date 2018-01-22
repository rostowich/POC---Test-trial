package com.crossover.techtrial.java.se.objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class UserLogin {

	private String email;

	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserLogin(String email, String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public UserLogin() {
		super();
		// TODO Auto-generated constructor stub
	}
}
