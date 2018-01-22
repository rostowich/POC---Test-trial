package com.crossover.techtrial.java.se.services;

import org.springframework.stereotype.Component;

import com.crossover.techtrial.java.se.domain.Account;

/**
 * This interface groups all the business operations concerning the accounts
 * @author 
 *
 */
@Component
public interface IAccountService {
	
	/**
	 * This method save the account into the database
	 * @param account
	 * @return
	 */
	public Account save(Account account);
		
}
