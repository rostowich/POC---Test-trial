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
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.techtrial.java.se.dao.IAccountDao;
import com.crossover.techtrial.java.se.dao.IUserDao;
import com.crossover.techtrial.java.se.domain.Account;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.exceptions.UserAlreadyExistException;
import com.crossover.techtrial.java.se.exceptions.UserNotFoundException;
import com.crossover.techtrial.java.se.objects.AccountResponse;
import com.crossover.techtrial.java.se.objects.Balance;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.objects.Role;

/**
 * This class is used to unit test the UserService class
 * @author
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserServiceTest {
	
	//We will mock the abstract method and any method calling web services of external
	//system for unit tests
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@Mock
	private IUserDao userDao;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	
	@Mock
	private IExternalSystemService externalSystemService;
	
	@Mock
	private IAccountDao accountDao;
	
	@Mock
	private Environment environment;

	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void register_ShouldReturnUserAlreadyExistException(){
		User us=new User("crossover@crossover.com", "crossover", Role.USER);
		Optional<User> user=Optional.of(us);
		
		//Mock the findByEmail Method of UserDao interface
		when(userDao.findByEmail("crossover@crossover.com")).thenReturn(user);
		try{
		userService.register(user.get());	
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(UserAlreadyExistException.class)
            .hasMessage("There is already a user with this email address");
		}
	}
	
	@Test
	public void register_ShouldReturnAirlineApiException_WhenCreatingTheAccount() throws AirlineApiException{
		User us=new User("crossover@crossover.com", "crossover", Role.USER);
		Optional<User> user=Optional.of(us);
		
		//Mock the findByEmail Method of UserDao interface
		when(userDao.findByEmail("crossover@crossover.com")).thenReturn(Optional.empty());
		//Mock the getAccountFromAPI of utilityService
		when(environment.getProperty("initial.accountCurency")).thenReturn("USD");
		when(externalSystemService.getAccountFromAPI(Currency.USD)).thenThrow(new AirlineApiException());
		try{
		userService.register(user.get());	
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessage("Error when creating the account from the external system");
		}
	}
	
	@Test
	public void register_ShouldReturnAirlineApiException_WhenConvertingCurrencies() throws AirlineApiException{
		User us=new User("crossover@crossover.com", "crossover", Role.USER);
		us.setAccount(new Account("test123", 0L, Currency.USD));
		Optional<User> user=Optional.of(us);
		
		//Mock the findByEmail Method of UserDao interface
		when(userDao.findByEmail("crossover@crossover.com")).thenReturn(Optional.empty());
		//Mock the getAccountFromAPI of utilityService
		AccountResponse accountResponse=new AccountResponse(new Balance(0L, Currency.USD), "test123");
		when(environment.getProperty("initial.accountCurency")).thenReturn("USD");
		when(externalSystemService.getAccountFromAPI(Currency.USD)).thenReturn(accountResponse);
		//Mock the moneyExchange method of utilityService
		when(environment.getProperty("initial.amount")).thenReturn("1000");
		when(externalSystemService.moneyExchange(1000L, Currency.USD, Currency.USD)).thenThrow(new AirlineApiException());	
		try{
		userService.register(user.get());	
		}
		catch(Exception e){
		assertThat(e)
        .isInstanceOf(AirlineApiException.class)
        .hasMessage("Error when converting currencies from the external system");
		}
	}
	
	@Test
	public void register_ShouldReturnAirlineApiException_WhenCreditingAccount() throws AirlineApiException{
		User us=new User("crossover@crossover.com", "crossover", Role.USER);
		us.setAccount(new Account("test123", 0L, Currency.USD));
		Optional<User> user=Optional.of(us);
		
		//Mock the findByEmail Method of UserDao interface
		when(userDao.findByEmail("crossover@crossover.com")).thenReturn(Optional.empty());
		//Mock the getAccountFromAPI of utilityService
		AccountResponse accountResponse=new AccountResponse(new Balance(0L, Currency.USD), "test123");
		when(environment.getProperty("initial.accountCurency")).thenReturn("USD");
		when(externalSystemService.getAccountFromAPI(Currency.USD)).thenReturn(accountResponse);
		//Mock the moneyExchange method of utilityService
		Balance monetaryAmount=new Balance(1000L, Currency.USD);
		when(environment.getProperty("initial.amount")).thenReturn("1000");
		when(externalSystemService.moneyExchange(1000L, Currency.USD, Currency.USD)).thenReturn(monetaryAmount);
		//Mock the creditAccountFromAPI of utilityService
		when(externalSystemService.creditAccountFromAPI("test123", 1000L, Currency.USD)).thenThrow(new AirlineApiException());
		try{
		userService.register(user.get());	
		}
		catch(Exception e){
			assertThat(e)
            .isInstanceOf(AirlineApiException.class)
            .hasMessage("Error when crediting the account from the external system");
		}
	}
	
	@Test
	public void register_ShouldReturnTheUserSaved() throws AirlineApiException, UserAlreadyExistException{
		User us=new User("crossover@crossover.com", "password");
		Account account=new Account("test123", 0L, Currency.USD);
		us.setAccount(account);
		Optional<User> user=Optional.of(us);
		
		///Mock the findByEmail Method of UserDao interface
		when(userDao.findByEmail("crossover@crossover.com")).thenReturn(Optional.empty());
		//Mock the getAccountFromAPI of utilityService
		AccountResponse accountResponse=new AccountResponse(new Balance(0L, Currency.USD), "test123");
		when(environment.getProperty("initial.accountCurency")).thenReturn("USD");
		when(externalSystemService.getAccountFromAPI(Currency.USD)).thenReturn(accountResponse);
		//Mock the moneyExchange method of utilityService
		Balance monetaryAmount=new Balance(1000L, Currency.USD);
		when(environment.getProperty("initial.amount")).thenReturn("1000");
		when(externalSystemService.moneyExchange(1000L, Currency.USD, Currency.USD)).thenReturn(monetaryAmount);
		//Mock the creditAccountFromAPI of utilityService
		accountResponse.getBalance().setAmount(1000L);
		when(externalSystemService.creditAccountFromAPI("test123", 1000L, Currency.USD)).thenReturn(accountResponse);
		//Mock the save method of accountDao
		account.setBalance(1000L);
		when(accountDao.save(refEq(account))).thenReturn(account);
		//Mock the save method of the userDao
		when(passwordEncoder.encode("password")).thenReturn("passwordHash");
		user.get().setRole(Role.USER);
		when(userDao.save(refEq(user.get(), "userId"))).thenReturn(user.get());
		
		User userSaved=userService.register(user.get());
		assertThat(userSaved.getEmail()).isEqualTo("crossover@crossover.com");
		assertThat(userSaved.getPassword()).isEqualTo("passwordHash");
		assertThat(userSaved.getRole().toString()).isEqualTo(Role.USER.toString());
		assertThat(userSaved.getAccount().getId()).isEqualTo("test123");
		assertThat(userSaved.getAccount().getBalance()).isEqualTo(1000L);
		assertThat(userSaved.getAccount().getCurrency().toString()).isEqualTo(Currency.USD.toString());
	}
	
	@Test
	public void getUserByEmail_ShouldReturn_UserNotFoundException(){
		
		//Mock the findByEmail Method of UserDao interface
		when(userDao.findByEmail("crossover@crossover.com")).thenReturn(Optional.empty());
		try{
			userService.getUserByEmail("crossover@crossover.com");	
			}
			catch(Exception e){
				assertThat(e)
	            .isInstanceOf(UserNotFoundException.class)
	            .hasMessage("User not found");
			}
	}
	
	@Test
	public void getUserByEmail_ShouldReturnAUser() throws UserNotFoundException{
		
		User us=new User("crossover@crossover.com", "crossover",Role.USER);
		Optional<User> user=Optional.of(us);
		
		//Mock the findByEmail Method of UserDao interface
		when(userDao.findByEmail("crossover@crossover.com")).thenReturn(user);
		
		Optional<User> userGot=userService.getUserByEmail("crossover@crossover.com");
		assertThat(userGot.get().getEmail()).isEqualTo("crossover@crossover.com");
		assertThat(userGot.get().getRole()).isEqualTo(Role.USER);
	}
	
	@Test
	public void getUserById_ShouldReturnEmpty() throws UserNotFoundException{
		
		//Mock the findByEmail Method of UserDao interface
		when(userDao.findByUserId(2L)).thenReturn(Optional.empty());
		
		Optional<User> userGot=userService.getUserById(2L);
		assertThat(!userGot.isPresent());
	}
	
	@Test
	public void getUserById_ShouldReturnAUser() throws UserNotFoundException{
		
		User us=new User("crossover@crossover.com", "crossover",Role.USER);
		us.setUserId(1L);
		Optional<User> user=Optional.of(us);
		
		//Mock the findByEmail Method of UserDao interface
		when(userDao.findByUserId(1L)).thenReturn(user);
		
		Optional<User> userGot=userService.getUserById(1L);
		assertThat(userGot.get().getEmail()).isEqualTo("crossover@crossover.com");
		assertThat(userGot.get().getRole().name()).isEqualTo(Role.USER.name());
	}
	
	@Test
	public void getAllUserByEmail(){
		User user1=new User("crossover@crossover.com", "crossover1",Role.USER);
		User user2=new User("user1@crossover.com", "crossover2",Role.USER);
		User user3=new User("user2@crossover.com", "crossover3",Role.USER);
		List<User> listUser=new ArrayList<User>();
		listUser.add(user1);
		listUser.add(user2);
		listUser.add(user3);
		Page<User> page=new PageImpl<>(listUser);
		
		//Mocking the findByEmailContaining method of UserDao interface
		when(userDao.findByEmailContaining("crossover", new PageRequest(0, 3))).thenReturn(page);
		
		//Now we can test the getAllUserByEmail method of the UserService component
		Page<User> pageUser=userService.getAllUserByEmail("crossover", new PageRequest(0, 3));
		assertThat(pageUser.getTotalElements()).isEqualTo(3);
		assertThat(pageUser.getNumberOfElements()).isEqualTo(3);
		assertThat(pageUser.getContent().size()).isEqualTo(3);
		assertThat(pageUser.getTotalPages()).isEqualTo(1);
	}
	
	@Test
	public void getAllUser(){
		User user1=new User("crossover@crossover.com", "crossover1",Role.USER);
		User user2=new User("user1@crossover.com", "crossover2",Role.USER);
		User user3=new User("user2@crossover.com", "crossover3",Role.USER);
		List<User> listUser=new ArrayList<User>();
		listUser.add(user1);
		listUser.add(user2);
		listUser.add(user3);
		Page<User> page=new PageImpl<>(listUser);
		
		//Mocking the findAllg method of UserDao interface
		when(userDao.findAll(new PageRequest(0, 3))).thenReturn(page);
		
		//Now we can test the getAllUserByEmail method of the UserService component
		Page<User> pageUser=userService.getAllUser(new PageRequest(0, 3));
		assertThat(pageUser.getTotalElements()).isEqualTo(3);
		assertThat(pageUser.getNumberOfElements()).isEqualTo(3);
		assertThat(pageUser.getContent().size()).isEqualTo(3);
		assertThat(pageUser.getTotalPages()).isEqualTo(1);
	}

}
