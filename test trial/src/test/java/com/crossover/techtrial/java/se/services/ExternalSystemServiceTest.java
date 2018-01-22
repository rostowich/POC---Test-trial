package com.crossover.techtrial.java.se.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.objects.AccountResponse;
import com.crossover.techtrial.java.se.objects.AirlineOffer;
import com.crossover.techtrial.java.se.objects.AirlineRoute;
import com.crossover.techtrial.java.se.objects.Balance;
import com.crossover.techtrial.java.se.objects.ByeTicketResponse;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.objects.UrlProvider;

/**
 * This class is used to unit test ExternalSystemService
 * @author
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ExternalSystemServiceTest {
	
	//We will mock the abstract method and any method calling web services of external
    //system for unit tests
	
	@Autowired
	private ExternalServiceImpl externalSystemService;
	
	@Autowired
	private RestTemplate restTemplateMock;

	private MockRestServiceServer mockServer;
	
	/**
	 * Setup the mockRestServiceServer 
	 */
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		 this.mockServer=MockRestServiceServer.createServer(restTemplateMock);
	}
	
	/**
	 * Test the create account method which return error code
	 */
	@Test
	public void getAccountFromAPI_ShouldThrowAirlineException_WhenNotFoundError(){
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToCreateAccount()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withStatus(HttpStatus.NOT_FOUND));
		try{
		externalSystemService.getAccountFromAPI(Currency.USD);
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the create account method which return error code
	 */
	@Test
	public void getAccountFromAPI_ShouldThrowAirlineException_WhenBadRequestError(){
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToCreateAccount()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withStatus(HttpStatus.BAD_REQUEST));
		try{
		externalSystemService.getAccountFromAPI(Currency.USD);
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the create account method which return the account
	 * @throws AirlineApiException 
	 */
	@Test
	public void getAccountFromAPI_ShouldReturnSuccessResponse() throws AirlineApiException{
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToCreateAccount()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withSuccess("{\"id\":\"pgdg435iu\",\"balance\":{\"amount\":0,\"currency\":\"USD\"}}"
	    				  	, MediaType.APPLICATION_JSON));
	    
	    AccountResponse accountResponse=externalSystemService.getAccountFromAPI(Currency.USD);
	    mockServer.verify();
	    assertThat(accountResponse.getId()).isEqualTo("pgdg435iu");
	    assertThat(accountResponse.getBalance().getAmount()).isEqualTo(0L);
	    assertThat(accountResponse.getBalance().getCurrency()).isEqualTo(Currency.USD);
	}
	
	/**
	 * Test the get offer method  which return error code
	 */
	@Test
	public void getOffersFromAPI_ShouldThrowAirlineException_WhenNotFoundError(){
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToGetOffer()))
	    		  .andExpect(method(HttpMethod.GET))
	    		  .andRespond(withStatus(HttpStatus.NOT_FOUND));
		try{
		externalSystemService.getOffers();
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the get offer method  which return error code
	 */
	@Test
	public void getOffersFromAPI_ShouldThrowAirlineException_WhenBadRequest(){
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToGetOffer()))
	    		  .andExpect(method(HttpMethod.GET))
	    		  .andRespond(withStatus(HttpStatus.BAD_REQUEST));
		try{
		externalSystemService.getOffers();
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the getOffers method which return the list of available offers from external system
	 * @throws AirlineApiException 
	 */
	@Test
	public void getOffersFromAPI_ShouldReturnSuccessResponse() throws AirlineApiException{
		//Create the list of 2 offers
		String response="[{\"route\":{\"from\":\"London\",\"to\":\"Madrid\"},\"price\":{\"amount\":100,\"currency\":\"USD\"}},"+
				"{\"route\":{\"from\":\"Paris\",\"to\":\"London\"},\"price\":{\"amount\":150,\"currency\":\"EUR\"}}]";
		System.out.println("response "+response);		
	    mockServer.expect(requestTo(UrlProvider.getUrlToGetOffer()))
	    		  .andExpect(method(HttpMethod.GET))
	    		  .andRespond(withSuccess(response
	    				  	, MediaType.APPLICATION_JSON));
	    //Now we can test the getOffer method
	    List<AirlineOffer> offers=externalSystemService.getOffers();
	    mockServer.verify();
	    
	    assertThat(offers.isEmpty()).isFalse();
	    assertThat(offers.size()).isEqualTo(2);
	    
	    assertThat(offers.get(0).getPrice().getAmount()).isEqualTo(100);
	    assertThat(offers.get(0).getPrice().getCurrency()).isEqualTo(Currency.USD);
	    assertThat(offers.get(0).getRoute().getFrom()).isEqualTo("London");
	    assertThat(offers.get(0).getRoute().getTo()).isEqualTo("Madrid");
	    
	    assertThat(offers.get(1).getPrice().getAmount()).isEqualTo(150);
	    assertThat(offers.get(1).getPrice().getCurrency()).isEqualTo(Currency.EUR);
	    assertThat(offers.get(1).getRoute().getFrom()).isEqualTo("Paris");
	    assertThat(offers.get(1).getRoute().getTo()).isEqualTo("London");

	}
	
	/**
	 * Test the deposit money into an account method which return error code
	 */
	@Test
	public void creditAccountFromAPI_ShouldThrowAirlineException_WhenNotFoundError(){
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToDepositInAccount()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withStatus(HttpStatus.NOT_FOUND));
		try{
		externalSystemService.creditAccountFromAPI("pgdg435iu", 1000L, Currency.USD);
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the deposit money into an account method which return error code
	 */
	@Test
	public void creditAccountFromAPI_ShouldThrowAirlineException_WhenBadRequest(){
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToDepositInAccount()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withStatus(HttpStatus.BAD_REQUEST));
		try{
		externalSystemService.creditAccountFromAPI("pgdg435iu", 1000L, Currency.USD);
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the create account method which return the account credited
	 * @throws AirlineApiException 
	 */
	@Test
	public void creditAccountFromAPI_ShouldReturnSuccessResponse() throws AirlineApiException{
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToDepositInAccount()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withSuccess("{\"id\":\"pgdg435iu\",\"balance\":{\"amount\":1000,\"currency\":\"USD\"}}"
	    				  	, MediaType.APPLICATION_JSON));
	    
	    AccountResponse accountResponse=externalSystemService.creditAccountFromAPI("pgdg435iu", 1000L, Currency.USD);
	    mockServer.verify();
	    assertThat(accountResponse.getId()).isEqualTo("pgdg435iu");
	    assertThat(accountResponse.getBalance().getAmount()).isEqualTo(1000L);
	    assertThat(accountResponse.getBalance().getCurrency()).isEqualTo(Currency.USD);
		
	}
	
	/**
	 * Test the purchase ticket method  which return error code
	 */
	@Test
	public void purchaseTicketFromApi_ShouldThrowAirlineException_WhenNotFoundError(){
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToPurchaseTicket()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withStatus(HttpStatus.NOT_FOUND));
		try{
		//Try to purchase 2 tickets for the same offer
		externalSystemService.byeTicketFromApi(2, new AirlineOffer(new Balance(100L, Currency.EUR), new AirlineRoute("London", "Madrid")), "pgdg435iu");
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the purchase ticket method  which return error code
	 */
	@Test
	public void byeTicketFromApi_ShouldThrowAirlineException_WhenBadRequest(){
		
		mockServer.expect(requestTo(UrlProvider.getUrlToPurchaseTicket()))
		  .andExpect(method(HttpMethod.POST))
		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		  .andRespond(withStatus(HttpStatus.BAD_REQUEST));
		try{
		//Try to purchase 2 tickets for the same offer
		externalSystemService.byeTicketFromApi(2, new AirlineOffer(new Balance(100L, Currency.EUR), new AirlineRoute("London", "Madrid")), "pgdg435iu");
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the purchase ticket method which return the tickets purchased
	 * @throws AirlineApiException 
	 */
	@Test
	public void byeTicketFromApi_ShouldReturnSuccessResponse() throws AirlineApiException{
		
		String response="{\"details\":{\"route\":{\"from\":\"London\",\"to\":\"Madrid\"},\"price\":{\"amount\":100,\"currency\":\"USD\"}},\"amount\":2}";
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToPurchaseTicket()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
	    
	    AirlineOffer offer= new AirlineOffer(new Balance(100L, Currency.EUR), new AirlineRoute("London", "Madrid")); 
	    
	    ByeTicketResponse ticketResponse=externalSystemService.byeTicketFromApi(2, offer, "pgdg435iu");
	    mockServer.verify();
	    assertThat(ticketResponse).isNotNull();
	    assertThat(ticketResponse.getAmount()).isEqualTo(2);
	    assertThat(ticketResponse.getDetails().getPrice().getAmount()).isEqualTo(100L);
	    assertThat(ticketResponse.getDetails().getPrice().getCurrency()).isEqualTo(Currency.USD);
	    assertThat(ticketResponse.getDetails().getRoute().getFrom()).isEqualTo("London");
	    assertThat(ticketResponse.getDetails().getRoute().getTo()).isEqualTo("Madrid");
	}
	
	/**
	 * Test the money exchange method  which return error code
	 */
	@Test
	public void moneyExchange_ShouldThrowAirlineException_WhenNotFoundError(){
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToExchangeMoney()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withStatus(HttpStatus.NOT_FOUND));
		try{
		externalSystemService.moneyExchange(100L, Currency.EUR, Currency.USD);
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the money exchange method  which return error code
	 */
	@Test
	public void moneyExchange_ShouldThrowAirlineException_WhenBadRequest(){
		
		mockServer.expect(requestTo(UrlProvider.getUrlToExchangeMoney()))
		  .andExpect(method(HttpMethod.POST))
		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		  .andRespond(withStatus(HttpStatus.BAD_REQUEST));
		try{
		externalSystemService.moneyExchange(100L, Currency.EUR, Currency.USD);
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the money exchange method which return the Currency exchanged
	 * @throws AirlineApiException 
	 */
	@Test
	public void moneyExchange_ShouldReturnSuccessResponse() throws AirlineApiException{
		
		String response="{\"amount\":110,\"currency\":\"USD\"}";
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToExchangeMoney()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
	    
	    Balance exchange=externalSystemService.moneyExchange(100L, Currency.EUR, Currency.USD);
	    mockServer.verify();
	    assertThat(exchange).isNotNull();
	    assertThat(exchange.getAmount()).isEqualTo(110);
	    assertThat(exchange.getCurrency()).isEqualTo(Currency.USD);
	}
	
	
	/**
	 * Test the withdraw money into an account method which return error code
	 */
	@Test
	public void WithdrawMoneyFromAccountFromAPI_ShouldThrowAirlineException_WhenNotFoundError(){
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToWithdrawMoneyFromAccount()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withStatus(HttpStatus.NOT_FOUND));
		try{
		externalSystemService.WithdrawMoneyFromAccountFromAPI("pgdg435iu", 100L, Currency.USD);
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the withdraw money into an account method which return error code
	 */
	@Test
	public void WithdrawMoneyFromAccountFromAPI_ShouldThrowAirlineException_WhenBadRequest(){
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToWithdrawMoneyFromAccount()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withStatus(HttpStatus.BAD_REQUEST));
		try{
		externalSystemService.WithdrawMoneyFromAccountFromAPI("pgdg435iu", 100L, Currency.USD);
		mockServer.verify();
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessageContaining("Bad request error from the external system");
			
		}
	}
	
	/**
	 * Test the withdraw money method which return the account credited
	 * @throws AirlineApiException 
	 */
	@Test
	public void WithdrawMoneyFromAccountFromAPI_ShouldReturnSuccessResponse() throws AirlineApiException{
		
	    mockServer.expect(requestTo(UrlProvider.getUrlToDepositInAccount()))
	    		  .andExpect(method(HttpMethod.POST))
	    		  .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	    		  .andRespond(withSuccess("{\"id\":\"pgdg435iu\",\"balance\":{\"amount\":900,\"currency\":\"USD\"}}"
	    				  	, MediaType.APPLICATION_JSON));
	    
	    AccountResponse accountResponse=externalSystemService.creditAccountFromAPI("pgdg435iu", 100L, Currency.USD);
	    mockServer.verify();
	    assertThat(accountResponse.getId()).isEqualTo("pgdg435iu");
	    assertThat(accountResponse.getBalance().getAmount()).isEqualTo(900L);
	    assertThat(accountResponse.getBalance().getCurrency()).isEqualTo(Currency.USD);
		
	}
}
