package com.crossover.techtrial.java.se.services;

import java.util.List;

import org.springframework.stereotype.Component;

import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.objects.AccountResponse;
import com.crossover.techtrial.java.se.objects.AirlineOffer;
import com.crossover.techtrial.java.se.objects.Balance;
import com.crossover.techtrial.java.se.objects.ByeTicketResponse;
import com.crossover.techtrial.java.se.objects.Currency;

/**
 * This interface groups all the business operations concerning the External API system
 * @author 
 *
 */
@Component
public interface IExternalSystemService {

	/**
	 * get all the accounts created from the External System
	 * @param accountRequest
	 * @return
	 */
	public  AccountResponse getAccountFromAPI(Currency currency) throws AirlineApiException;
	
	/**
	 * Credit a specific account from the External System
	 * @param dep
	 * @return
	 */
	public AccountResponse creditAccountFromAPI(String accountId, Long amount, Currency curency) throws AirlineApiException;
	
	/**
	 * Get all the offers available from the external system
	 * @return
	 */
	public List<AirlineOffer> getOffers() throws AirlineApiException;
	
	/**
	 * Purchase a ticket from the External system
	 * @param byeTicketRequest
	 * @return
	 */
	public  ByeTicketResponse byeTicketFromApi(Integer number, AirlineOffer offer, String accountId) throws AirlineApiException;
	
	/**
	 * Perform the money exchange service for different currencies
	 * @param exchangeRequest
	 * @return
	 */
	public  Balance moneyExchange(Long amount, Currency source, Currency target) throws AirlineApiException;

	/**
	 * Withdraw money from an account from the external system
	 * @param accountId
	 * @param amount
	 * @param curency
	 * @return
	 * @throws AirlineApiException
	 */
	public AccountResponse WithdrawMoneyFromAccountFromAPI(String accountId, Long amount, Currency curency) throws AirlineApiException;

	/**
	 * Reset the data from the external system
	 * @throws AirlineApiException
	 */
	public void  reset() throws AirlineApiException;
}


