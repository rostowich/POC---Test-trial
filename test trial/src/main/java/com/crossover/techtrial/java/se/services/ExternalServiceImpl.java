package com.crossover.techtrial.java.se.services;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.objects.AccountRequest;
import com.crossover.techtrial.java.se.objects.AccountResponse;
import com.crossover.techtrial.java.se.objects.AirlineOffer;
import com.crossover.techtrial.java.se.objects.Balance;
import com.crossover.techtrial.java.se.objects.ByeTicketRequest;
import com.crossover.techtrial.java.se.objects.ByeTicketResponse;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.objects.DepositAccountRequest;
import com.crossover.techtrial.java.se.objects.ExchangeRequest;
import com.crossover.techtrial.java.se.objects.UrlProvider;
import com.crossover.techtrial.java.se.objects.WithdrawAccountRequest;

/**
 * This is the implementation for IExternalService
 * @author
 *
 */
@Service
public class ExternalServiceImpl implements IExternalSystemService{
	
	private static final Logger logger = LoggerFactory.getLogger(ExternalServiceImpl.class);
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
	   // Do any additional configuration here
	   return builder.build();
	}

	@Autowired
	private RestTemplate restTemplate;

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IExternalSystemService#getAccountFromAPI(com.crossover.techtrial.java.se.objects.Currency)
	 */
	@Override
	public AccountResponse getAccountFromAPI(Currency currency) throws AirlineApiException {
		AccountRequest accountRequest=new AccountRequest(UrlProvider.getTheApplicantId(), currency);
		ResponseEntity<AccountResponse> responseEntity;
		try{
			responseEntity=restTemplate.postForEntity(UrlProvider.getUrlToCreateAccount(), accountRequest, AccountResponse.class);
		if(!responseEntity.getStatusCode().equals(HttpStatus.OK)){
			logger.info("Bad request error from the external system: reason "+responseEntity.getStatusCode().getReasonPhrase());
			throw new  AirlineApiException("Bad request error from the external system: reason "+responseEntity.getStatusCode().getReasonPhrase());
		}
		}
		catch(HttpClientErrorException e){
			logger.info("Bad request error from the external system: reason "+e.getResponseBodyAsString());
			throw new  AirlineApiException("Bad request error from the external system: reason "+e.getResponseBodyAsString());
		}

		return responseEntity.getBody();
	}

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IExternalSystemService#creditAccountFromAPI(java.lang.String, java.lang.Long, com.crossover.techtrial.java.se.objects.Currency)
	 */
	@Override
	public AccountResponse creditAccountFromAPI(String accountId, Long amount, Currency curency) throws AirlineApiException {

		DepositAccountRequest depositAccountRequest=new DepositAccountRequest();
		depositAccountRequest.setAccountId(accountId);
		depositAccountRequest.setApplicantId(UrlProvider.getTheApplicantId());
		depositAccountRequest.setMonetaryAmount(new Balance(amount, curency));
		
		ResponseEntity<AccountResponse> responseEntity;
		try{
		responseEntity= restTemplate.postForEntity(UrlProvider.getUrlToDepositInAccount(), depositAccountRequest, AccountResponse.class);
		
		if(!responseEntity.getStatusCode().equals(HttpStatus.OK)){
			//throw the exception for error 400
			logger.info("Bad request error from the external system: reason "+responseEntity.getStatusCode().getReasonPhrase());
			throw new  AirlineApiException("Bad request error from the external system: reason "+responseEntity.getStatusCode().getReasonPhrase());
		}		
		}
		catch(HttpClientErrorException e){
			//throw the exception for error 404
			logger.info("Bad request error from the external system "+e.getResponseBodyAsString());
			throw new  AirlineApiException("Bad request error from the external system "+e.getResponseBodyAsString());
		}

		return responseEntity.getBody();
	}

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IExternalSystemService#getOffers()
	 */
	@Override
	public List<AirlineOffer> getOffers() throws AirlineApiException {

		ResponseEntity<AirlineOffer[]> responseEntity;
		try{
			responseEntity=restTemplate.getForEntity(UrlProvider.getUrlToGetOffer(), AirlineOffer[].class);
		}
		catch(HttpClientErrorException e){
			//throw the exception for error 404
			logger.info("Bad request error from the external system "+e.getResponseBodyAsString());
			throw new  AirlineApiException("Bad request error from the external system "+e.getResponseBodyAsString());
		}
		
		return Arrays.asList(responseEntity.getBody());
	}

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IExternalSystemService#byeTicketFromApi(java.lang.Integer, com.crossover.techtrial.java.se.objects.AirlineOffer, java.lang.String)
	 */
	@Override
	public ByeTicketResponse byeTicketFromApi(Integer number, AirlineOffer offer, String accountId) throws AirlineApiException {
		
		ByeTicketRequest byeTicketRequest=new ByeTicketRequest();
		byeTicketRequest.setApplicantId(UrlProvider.getTheApplicantId());
		byeTicketRequest.setAccountId(accountId);
		byeTicketRequest.setRoute(offer.getRoute());
		byeTicketRequest.setAmount(number);
		
		ResponseEntity<ByeTicketResponse> responseEntity;
		try{
			responseEntity= restTemplate.postForEntity(UrlProvider.getUrlToPurchaseTicket(), byeTicketRequest, ByeTicketResponse.class);
			if(!responseEntity.getStatusCode().equals(HttpStatus.OK)){
				//throw the exception for error 400
				logger.info("Bad request error from the external system "+responseEntity.getStatusCode().getReasonPhrase());
				throw new  AirlineApiException("Bad request error from the external system "+responseEntity.getStatusCode().getReasonPhrase());
			}
		}
		catch(HttpClientErrorException e){
			//throw the exception for error 404
			logger.info("Bad request error from the external system "+e.getResponseBodyAsString());
			throw new  AirlineApiException("Bad request error from the external system "+e.getResponseBodyAsString());
		}
		
		return responseEntity.getBody();
	}

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IExternalSystemService#moneyExchange(java.lang.Long, com.crossover.techtrial.java.se.objects.Currency, com.crossover.techtrial.java.se.objects.Currency)
	 */
	@Override
	public Balance moneyExchange(Long amount, Currency source, Currency target) throws AirlineApiException {

		ExchangeRequest exchangeRequest=new ExchangeRequest();
		exchangeRequest.setApplicantId(UrlProvider.getTheApplicantId());
		exchangeRequest.setTargetCurrency(target);
		Balance monetaryAmount=new Balance(amount, source);
		exchangeRequest.setMonetaryAmount(monetaryAmount);

		ResponseEntity<Balance> responseEntity;
		try{
			responseEntity=restTemplate.postForEntity(UrlProvider.getUrlToExchangeMoney(), exchangeRequest, Balance.class);
			if(!responseEntity.getStatusCode().equals(HttpStatus.OK)){
				//throw the exception for error 400
				logger.info("Bad request error from the external system "+responseEntity.getStatusCode().getReasonPhrase());
				throw new  AirlineApiException("Bad request error from the external system "+responseEntity.getStatusCode().getReasonPhrase());
			}
		}
		catch(HttpClientErrorException e){
			//throw the exception for error 404
			logger.info("Bad request error from the external system "+e.getResponseBodyAsString());
			throw new  AirlineApiException("Bad request error from the external system "+e.getResponseBodyAsString());

		}
	
		return responseEntity.getBody();
	}

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IExternalSystemService#WithdrawMoneyFromAccountFromAPI(java.lang.String, java.lang.Long, com.crossover.techtrial.java.se.objects.Currency)
	 */
	@Override
	public AccountResponse WithdrawMoneyFromAccountFromAPI(String accountId, Long amount, Currency curency)
			throws AirlineApiException {
		
		WithdrawAccountRequest withdrawAccountRequest=new WithdrawAccountRequest();
		withdrawAccountRequest.setAccountId(accountId);
		withdrawAccountRequest.setMonetaryAmount(new Balance(amount, curency));
		
		ResponseEntity<AccountResponse> responseEntity;
		try{
		responseEntity= restTemplate.postForEntity(UrlProvider.getUrlToWithdrawMoneyFromAccount(), withdrawAccountRequest, AccountResponse.class);
		
		if(!responseEntity.getStatusCode().equals(HttpStatus.OK)){
			//throw the exception for error 400
			logger.info("Bad request error from the external system "+responseEntity.getStatusCode().getReasonPhrase());
			throw new  AirlineApiException("Bad request error from the external system "+responseEntity.getStatusCode().getReasonPhrase());
		}		
		}
		catch(HttpClientErrorException e){
			//throw the exception for error 404
			logger.info("Bad request error from the external system "+e.getResponseBodyAsString());
			throw new  AirlineApiException("Bad request error from the external system "+e.getResponseBodyAsString());
		}

		return responseEntity.getBody();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IExternalSystemService#reset()
	 */
	@Override
	public void reset() throws AirlineApiException {
		
		try{
			restTemplate.getForEntity(UrlProvider.getUrlToResetApi(), Void.class);
		}
		catch(HttpClientErrorException e){
			logger.info("Bad request error from the external system "+e.getResponseBodyAsString());
			throw new  AirlineApiException("Bad request error from the external system "+e.getResponseBodyAsString());
		}
	}
}
