package com.crossover.techtrial.java.se.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.techtrial.java.se.dao.IAccountDao;
import com.crossover.techtrial.java.se.dao.ITicketDao;
import com.crossover.techtrial.java.se.dao.IUserDao;
import com.crossover.techtrial.java.se.dao.TicketSpecification;
import com.crossover.techtrial.java.se.domain.Account;
import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.exceptions.InvalidArgumentException;
import com.crossover.techtrial.java.se.exceptions.NotEnoughAmountInAccountException;
import com.crossover.techtrial.java.se.exceptions.UserNotFoundException;
import com.crossover.techtrial.java.se.objects.AccountResponse;
import com.crossover.techtrial.java.se.objects.Balance;
import com.crossover.techtrial.java.se.objects.Currency;

/**
 * This class is used to unit test the TicketService class
 * @author
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class TicketServiceTest {

	//We will mock the abstract method and any method calling web services of external
	//system for unit tests
	
	@InjectMocks
	private TicketServiceImpl ticketService;
	
	@Mock
	private ITicketDao ticketDao;
	
	@Mock
	private IUserDao userDao;
	
	@Mock
	private IExternalSystemService externalSystemService;
	
	@Mock
	private IAccountDao accountDao;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getTicket_ShouldReturn_Ticket(){
		
		Ticket ticket=new Ticket("London", "Madrid", 100L, Currency.EUR, 1);
		ticket.setId(1L);
		//Mock the findById method of ticketDao
		when(ticketDao.findOne(1L)).thenReturn(ticket);
		when(ticketDao.findOne(2L)).thenReturn(null);
		
		//Now we can test the getTicket method
		Ticket ticketGot=ticketService.getTicket(1L);
		assertThat(ticketGot).isInstanceOf(Ticket.class);
		assertThat(ticketGot.getId()).isEqualTo(1L);
		assertThat(ticketGot.getDeparture()).isEqualTo("London");
		assertThat(ticketGot.getDestination()).isEqualTo("Madrid");
		assertThat(ticketGot.getAmount()).isEqualTo(100L);
		
		Ticket ticketNull=ticketService.getTicket(2L);
		assertThat(ticketNull).isNull();
	}
	
	@Test
	public void findAllByExample(){
		Ticket ticket1=new Ticket("London", "Madrid", 100L, Currency.EUR, 1);
		Ticket ticket2=new Ticket("Paris", "Madrid", 100L, Currency.EUR, 1);
		List<Ticket> listTicket=new ArrayList<Ticket>();
		listTicket.add(ticket1);
		listTicket.add(ticket2);
		Page<Ticket> page=new PageImpl<>(listTicket);
		
		Ticket ticketSpec=new Ticket();
		ticketSpec.setAmount(100L);
		ticketSpec.setDestination("Madrid");
		Specification<Ticket> ticketSpecification=new TicketSpecification(ticketSpec);
		
		//Mocking the findAll method of UserDao interface
		
		when(ticketDao.findAll(refEq(ticketSpecification),refEq(new PageRequest(0, 2)))).thenReturn(page);
		
		//Now we can test the findAllByExample method of the UserService component
		Page<Ticket> pageTicket=ticketService.findAllByExample(ticketSpec, new PageRequest(0, 2));
		assertThat(pageTicket.getTotalElements()).isEqualTo(2);
		assertThat(pageTicket.getNumberOfElements()).isEqualTo(2);
		assertThat(pageTicket.getContent().size()).isEqualTo(2);
		assertThat(pageTicket.getTotalPages()).isEqualTo(1);
	}
	
	@Test
	public void findByUser_ShouldReturn_UserNotFoundException() throws UserNotFoundException{
		//Mock the findById method of ticketDao
		when(userDao.findByUserId(1L)).thenReturn(Optional.empty());
		
		//Now we can test the findByUser method
		try{
		ticketService.findByUser(1L, new PageRequest(0,5));
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(UserNotFoundException.class)
            .hasMessage("User not found");
		}
		
	}
	
	@Test
	public void findByUser_ShouldReturn_PageOfTicket() throws UserNotFoundException{
		User user=new User("crossover@crosover.com", "crossover");
		user.setUserId(1L);
		Ticket ticket1=new Ticket("London", "Madrid", 100L, Currency.EUR, 1);
		ticket1.setUser(user);
		Ticket ticket2=new Ticket("Paris", "Madrid", 300L, Currency.EUR, 1);
		ticket2.setUser(user);
		Ticket ticket3=new Ticket("Bruxelle", "Madrid", 200L, Currency.EUR, 1);
		ticket3.setUser(user);
		List<Ticket> listTicket=new ArrayList<Ticket>();
		listTicket.add(ticket1);
		listTicket.add(ticket2);
		listTicket.add(ticket3);
		Page<Ticket> page=new PageImpl<>(listTicket);
		
		//Mock the findByUserId Method of UserDao interface
		Optional<User> userOp=Optional.of(user);
		when(userDao.findByUserId(1L)).thenReturn(userOp);
		//Mock the findByUser method of ticketDao
		when(ticketDao.findByUser(user, new PageRequest(0, 3))).thenReturn(page);
		
		//Now we can test the findByUser service
		Page<Ticket> pageTicket=ticketService.findByUser(1L, new PageRequest(0,3));
		assertThat(pageTicket.getTotalElements()).isEqualTo(3);
		assertThat(pageTicket.getNumberOfElements()).isEqualTo(3);
		assertThat(pageTicket.getContent().size()).isEqualTo(3);
		assertThat(pageTicket.getTotalPages()).isEqualTo(1);
	}
	
	@Test
	public void testPurchaseTicket_ReturnUserNotFoundException(){
		Ticket ticket=new Ticket("London", "Madrid", 100L, Currency.USD, 1);
		User us=new User("crossover@crossover.com", "password");
		us.setUserId(1L);
		//Mock the findByUserId of the userDao
		when(userDao.findByUserId(1L)).thenReturn(Optional.empty());		
		try{
			ticketService.purchase(ticket, 1L);	
		}catch(Exception e){
			assertThat(e)
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("User not found");
		}
	}
	
	@Test
	public void testPurchaseTicket_ReturnAirlineApiException_WhenExchangingCurrencies() throws AirlineApiException{
		Ticket ticket=new Ticket("London", "Madrid", 100L, Currency.EUR, 1);
		User us=new User("crossover@crossover.com", "password");
		us.setUserId(1L);
		Account account=new Account("test123", 1000L, Currency.USD);
		us.setAccount(account);
		Optional<User> user=Optional.of(us);
		//Mock the findByUserId of the userDao
		when(userDao.findByUserId(1L)).thenReturn(user);	
		//Mock the moneyExchange of utilityService
		when(externalSystemService.moneyExchange(100L, Currency.EUR, Currency.USD)).thenThrow(new AirlineApiException());
		try{
			ticketService.purchase(ticket, 1L);	
			}
			catch(Exception e){
				assertThat(e)
	            .isInstanceOf(AirlineApiException.class)
	            .hasMessage("Error when converting currencies from the external system");
			}
	}
	
	@Test
	public void testPurchaseTicket_ReturnNotEnoughAmountInAccountException() throws AirlineApiException{
		Ticket ticket=new Ticket("London", "Madrid", 100L, Currency.EUR, 1);
		User us=new User("crossover@crossover.com", "password");
		us.setUserId(1L);
		Account account=new Account("test123", 10L, Currency.USD);
		us.setAccount(account);
		Optional<User> user=Optional.of(us);
		//Mock the findByUserId of the userDao
		when(userDao.findByUserId(1L)).thenReturn(user);	
		//Mock the moneyExchange of utilityService
		Balance balance=new Balance(110L, Currency.USD);
		when(externalSystemService.moneyExchange(100L, Currency.EUR, Currency.USD)).thenReturn(balance);
		//The user account balance is not enough to purchase the ticket
		try{
			ticketService.purchase(ticket, 1L);	
			}
			catch(Exception e){
				assertThat(e)
	            .isInstanceOf(NotEnoughAmountInAccountException.class)
	            .hasMessage("Not enough amount in the account");
			}
	}
	
	@Test
	public void testPurchaseTicket_ReturnAirlineApiException_WhenWithdrawingAccountFromApi() throws AirlineApiException{
		Ticket ticket=new Ticket("London", "Madrid", 100L, Currency.EUR, 1);
		User us=new User("crossover@crossover.com", "password");
		us.setUserId(1L);
		Account account=new Account("test123", 1000L, Currency.USD);
		us.setAccount(account);
		Optional<User> user=Optional.of(us);
		//Mock the findByUserId of the userDao
		when(userDao.findByUserId(1L)).thenReturn(user);	
		//Mock the moneyExchange of utilityService
		Balance balance=new Balance(110L, Currency.USD);
		when(externalSystemService.moneyExchange(100L, Currency.EUR, Currency.USD)).thenReturn(balance);
		//The user account balance is now enough to purchase a ticket
		//Mock the save method of the accountDao
		account.setBalance(890L);
		when(accountDao.save(account)).thenReturn(account);
		//Mock the WithdrawMoneyFromAccountFromAPI method of utilityService
		when(externalSystemService.WithdrawMoneyFromAccountFromAPI("test123", 110L, Currency.USD)).thenThrow(new AirlineApiException());
		try{
			ticketService.purchase(ticket, 1L);	
			}
			catch(Exception e){
				assertThat(e)
	            .isInstanceOf(AirlineApiException.class)
	            .hasMessage("Not enough amount in the account from the external system");
			}
	}
	
	@Test
	public void testPurchaseTicket_ReturnThePurchasedTicket() throws AirlineApiException, UserNotFoundException, NotEnoughAmountInAccountException, InvalidArgumentException{
		Ticket ticket=new Ticket("London", "Madrid", 100L, Currency.EUR, 1);
		User us=new User("crossover@crossover.com", "password");
		us.setUserId(1L);
		Account account=new Account("test123", 1000L, Currency.USD);
		us.setAccount(account);
		ticket.setUser(us);
		Optional<User> user=Optional.of(us);
		//Mock the findByUserId of the userDao
		when(userDao.findByUserId(1L)).thenReturn(user);	
		//Mock the moneyExchange of utilityService
		Balance balance=new Balance(110L, Currency.USD);
		when(externalSystemService.moneyExchange(100L, Currency.EUR, Currency.USD)).thenReturn(balance);
		//The user account balance is now enough to purchase a ticket
		Account accountSaved=new Account("test123", 890L, Currency.USD);
		when(accountDao.save(refEq(account))).thenReturn(accountSaved);
		//Mock the WithdrawMoneyFromAccountFromAPI method of utilityService
		AccountResponse accountResponse=new AccountResponse(new Balance(890L, Currency.USD), "test123");
		when(externalSystemService.WithdrawMoneyFromAccountFromAPI("test123", 110L, Currency.USD)).thenReturn(accountResponse);
		//Mock the save ticket
		when(ticketDao.save(refEq(ticket,"id"))).thenReturn(ticket);
		
		ticket=ticketService.purchase(ticket, 1L);	
		
		assertThat(ticket.getAmount()).isEqualTo(100L);
		assertThat(ticket.getCurrency()).isEqualTo(Currency.EUR);
		assertThat(ticket.getDeparture()).isEqualTo("London");
		assertThat(ticket.getDestination()).isEqualTo("Madrid");
		assertThat(ticket.getNumber()).isEqualTo(1);
		//verify that the account is debited
		assertThat(ticket.getUser().getAccount().getBalance()).isEqualTo(890L);
		assertThat(ticket.getUser().getUserId()).isEqualTo(1L);
			
	}
}
