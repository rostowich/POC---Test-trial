package com.crossover.techtrial.java.se.objects;


/**
 * This class is supposed to return the URL needed to invoke web service from 
 * the external system
 *
 */
public class UrlProvider {

	private static final String mainUrl ="https://api-forest.crossover.com/b153Xln";
			
	private static final String resourceToCreateAccount="/paypallets/account";
	
	private static final String resourceToDeposit="/paypallets/account/deposit";
	
	private static final String resourceToGetOffer="/gammaairlines/offers";	
	
	private static final String resourceToPurchaseTicket="/gammaairlines/tickets/buy";
	
	private static final String resourceToExchangeMoney="/moneyexchange/exchange";
	
	private static final String resourceToWithdrawMoney="/paypallets/account/withdraw";
	
	private static final String resourceToResetApi="/reset";
	
	private static final String applicantId="b153Xln";
	
	/**
	 * get the uri to create an account from the external system
	 * @return
	 */
	public static String getUrlToCreateAccount(){
		return mainUrl+resourceToCreateAccount;
	}
	
	/**
	 * get the uri to deposit an amount into the acccount from the external system
	 * @return
	 */
	public static String getUrlToDepositInAccount(){
		return mainUrl+resourceToDeposit;
	}
	
	/**
	 * get the uri to get all the available offers from the external system
	 * @return
	 */
	public static String getUrlToGetOffer(){
		return mainUrl+resourceToGetOffer;
	}
	
	/**
	 * get the uri to purchase a ticket from the external system
	 * @return
	 */
	public static String getUrlToPurchaseTicket(){
		return mainUrl+resourceToPurchaseTicket;
	}
	
	/**
	 * get the uri to exchange money from the external system
	 * @return
	 */
	public static String getUrlToExchangeMoney(){
		return mainUrl+resourceToExchangeMoney;
	}
	
	/**
	 * get the uri to withdraw money from the account from the external system
	 * @return
	 */
	public static String getUrlToWithdrawMoneyFromAccount(){
		return mainUrl+resourceToWithdrawMoney;
	}
	
	/**
	 * get the uri to reset data from the external system
	 * @return
	 */
	public static String getUrlToResetApi(){
		return mainUrl+resourceToResetApi;
	}
	
	/**
	 * get the uri to reset data from the external system
	 * @return
	 */
	public static String getTheApplicantId(){
		return applicantId;
	}
}
