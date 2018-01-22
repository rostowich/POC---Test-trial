package com.crossover.techtrial.java.se.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.crossover.techtrial.java.se.controllers.UserController;
import com.crossover.techtrial.java.se.domain.Account;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.exceptions.UserAlreadyExistException;
import com.crossover.techtrial.java.se.exceptions.UserNotFoundException;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.objects.Role;
import com.crossover.techtrial.java.se.objects.UserRegistration;
import com.crossover.techtrial.java.se.services.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class represents the unit test of the UserController class
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value=UserController.class, secure=false)
@ActiveProfiles("test")
public class UserControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	//We  have to add all these mocks to the spring ApplicationContext
	@MockBean
	private IUserService userService;
	
	@MockBean
	private Environment environment;
	
	@Test
	public void registerTestWithInvalidParameters() throws Exception{
		
	    //test the method with empty Email
		UserRegistration userWithEmptyEmailAndPassword=new UserRegistration("", "", "");
		RequestBuilder requestBuilderEmptyEmail = MockMvcRequestBuilders
				.post("/users/register")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(userWithEmptyEmailAndPassword))
				.contentType(MediaType.APPLICATION_JSON);
		String ExpectedResponseWithEmptyEmail="{\"message\":\"Email required\",\"type\":\"ERROR\"}";
		
		MvcResult resultEmptyEmail = mockMvc.perform(requestBuilderEmptyEmail).andReturn();
		MockHttpServletResponse responseEmptyEmail=resultEmptyEmail.getResponse();
		assertThat(responseEmptyEmail.getContentAsString()).isEqualTo(ExpectedResponseWithEmptyEmail);
		
		//test the method with empty password
		UserRegistration userRegistrationWithEmptyPassword=new UserRegistration("crossover@crossover.com", "", "");
		RequestBuilder requestBuilderEmptypassword = MockMvcRequestBuilders
				.post("/users/register")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(userRegistrationWithEmptyPassword))
				.contentType(MediaType.APPLICATION_JSON);
		String ExpectedResponseEmptyPassword="{\"message\":\"Password required\",\"type\":\"ERROR\"}";
		
		MvcResult resultEmptyPassword = mockMvc.perform(requestBuilderEmptypassword).andReturn();
		MockHttpServletResponse responseEmptyPassword=resultEmptyPassword.getResponse();
		assertThat(responseEmptyPassword.getContentAsString()).isEqualTo(ExpectedResponseEmptyPassword);

		//test the method with empty invalid email
		UserRegistration userRegistrationWithInvalidEmail=new UserRegistration("crossover", "test123", "test123");
		RequestBuilder requestBuilderInvalidEmail = MockMvcRequestBuilders
				.post("/users/register")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(userRegistrationWithInvalidEmail))
				.contentType(MediaType.APPLICATION_JSON);
		String ExpectedResponseInvalidEmail="{\"message\":\"The email address is invalide\",\"type\":\"ERROR\"}";
		
		MvcResult resultInvalidEmail = mockMvc.perform(requestBuilderInvalidEmail).andReturn();
		MockHttpServletResponse responseInvalidEmail=resultInvalidEmail.getResponse();
		assertThat(responseInvalidEmail.getContentAsString()).isEqualTo(ExpectedResponseInvalidEmail);

		//test the method with password do not match
		UserRegistration userRegistrationWithPasswordDoesNotMatch=new UserRegistration("crossover@crossover.com", "password", "password123");
		RequestBuilder requestBuilderPasswordDoesNotMatch = MockMvcRequestBuilders
				.post("/users/register")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(userRegistrationWithPasswordDoesNotMatch))
				.contentType(MediaType.APPLICATION_JSON);
		String ExpectedResponsePasswordDoNotMatch="{\"message\":\"The passwords do not match\",\"type\":\"ERROR\"}";
		
		MvcResult resultPasswordDoNotMatch = mockMvc.perform(requestBuilderPasswordDoesNotMatch).andReturn();
		MockHttpServletResponse responseDoNotMatch=resultPasswordDoNotMatch.getResponse();
		assertThat(responseDoNotMatch.getContentAsString()).isEqualTo(ExpectedResponsePasswordDoNotMatch);
	}
	
	@Test
	public void registerTestInWhichTheUserAlreadyExists() throws Exception{
		
		User userExist=new User("crossover@crossover.com", "password");
		
		//Mock the register method of UserService
		when(userService.register(refEq(userExist, "userId","account","tickets","role"))).thenThrow(new UserAlreadyExistException("This User already exists"));
		try{
			//Now we can test the register method
			UserRegistration registrationWithUserAlreadyExists=new UserRegistration("crossover@crossover.com", "password", "password");
			RequestBuilder requestBuilderWithUserAlreadyExists = MockMvcRequestBuilders
					.post("/users/register")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content(objectMapper.writeValueAsBytes(registrationWithUserAlreadyExists))
					.contentType(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(requestBuilderWithUserAlreadyExists).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(UserAlreadyExistException.class)
			.hasMessage("This User already exists");
		}
	}
	
	@Test
	public void registerTestWhichReturnAirlineApiException() throws Exception{
				
		User userExist=new User("crossover@crossover.com", "password");
		
		//Mock the register method of UserService
		when(userService.register(refEq(userExist, "userId","account","tickets","role"))).thenThrow(new AirlineApiException("Issue with External system"));
		try{
			//Now we can test the register method
			RequestBuilder requestBuilderWithAirlineException = MockMvcRequestBuilders
					.post("/users/register")
					.accept(MediaType.APPLICATION_JSON_UTF8)
					.content("{\"email\": \"crossover@crossover.com\",\"password\": \"password\",\"confirmPassword\": \"password\"}")
					.contentType(MediaType.APPLICATION_JSON);
			
			mockMvc.perform(requestBuilderWithAirlineException).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(AirlineApiException.class)
			.hasMessage("Issue with External system");
		}
	}

	@Test
	public void registerTestWithUserCreated() throws Exception{
				
		User user=new User("crossover@crossover.com", "password");
		Account account=new Account("pdi76vdhMO", 1000L, Currency.USD);
		user.setRole(Role.USER);
		user.setAccount(account);
		//Mock the register method of UserService
		when(userService.register(refEq(user, "userId","account","tickets","role"))).thenReturn(user);
		
		//Now we can test the register method
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/users/register")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content("{\"email\": \"crossover@crossover.com\",\"password\": \"password\",\"confirmPassword\": \"password\"}")
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("crossover@crossover.com")))
				.andExpect(jsonPath("$.role", is(Role.USER.toString())))
				.andExpect(jsonPath("$.account.id", is("pdi76vdhMO")))
				.andExpect(jsonPath("$.account.balance", is(1000)));
	}
	
	@Test
	public void getAUser_ReturnUserNotFoundException() throws Exception{
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/users/1")
				.contentType(MediaType.APPLICATION_JSON);
		
		when(userService.getUserById(1L)).thenReturn(Optional.empty());
		
		try{
			//Now we can getOffers method
			mockMvc.perform(requestBuilder).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(UserNotFoundException.class)
			.hasMessage("User not found");
		}
	}
		
	@Test
	public void getAUser_ReturnAUser() throws Exception{
		
		Account account=new Account("test123", 1000L, Currency.USD);
		User user=new User("crossover@crossover.com", "password", Role.USER, account);
		user.setUserId(1L);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/users/1")
				.contentType(MediaType.APPLICATION_JSON);
		
		when(userService.getUserById(1L)).thenReturn(Optional.of(user));

		//Now we can getOffers method
		mockMvc.perform(requestBuilder)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.email", is("crossover@crossover.com")))
		.andExpect(jsonPath("$.role", is(Role.USER.toString())))
		.andExpect(jsonPath("$.account.id", is("test123")))
		.andExpect(jsonPath("$.account.currency", is(Currency.USD.toString())))
		.andExpect(jsonPath("$.account.balance", is(1000)));
	}
	
	@Test
	public void getAllAUser_ReturnTheListOfUser() throws Exception{
		
		Account account=new Account("test123", 1000L, Currency.USD);
		User user=new User("crossover@crossover.com", "password", Role.USER, account);
		user.setUserId(1L);
		Account account2=new Account("test1234", 800L, Currency.USD);
		User user2=new User("crossover2@crossover.com", "password", Role.USER, account2);
		user2.setUserId(2L);
		List<User> listUser=new ArrayList<User>();
		listUser.add(user);
		listUser.add(user2);
		Page<User> pageUser=new PageImpl<>(listUser);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/users/all?page=0")
				.contentType(MediaType.APPLICATION_JSON);
		
		when(environment.getProperty("pages.number")).thenReturn("5");
		when(userService.getAllUser(new PageRequest(0, 5))).thenReturn(pageUser);

		//Now we can test the getAllUserMethod
		mockMvc.perform(requestBuilder)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.totalElements", is(2)))
		.andExpect(jsonPath("$.totalPages", is(1)))
		.andExpect(jsonPath("$.number", is(0)))
		.andExpect(jsonPath("$.content[0].email", is("crossover@crossover.com")))
		.andExpect(jsonPath("$.content[0].role", is(Role.USER.toString())))
		.andExpect(jsonPath("$.content[0].account.id", is("test123")))
		.andExpect(jsonPath("$.content[0].account.balance", is(1000)))
		.andExpect(jsonPath("$.content[0].account.currency", is(Currency.USD.toString())))
		.andExpect(jsonPath("$.content[1].email", is("crossover2@crossover.com")))
		.andExpect(jsonPath("$.content[1].role", is(Role.USER.toString())))
		.andExpect(jsonPath("$.content[1].account.id", is("test1234")))
		.andExpect(jsonPath("$.content[1].account.balance", is(800)))
		.andExpect(jsonPath("$.content[1].account.currency", is(Currency.USD.toString())));
	}
	
	@Test
	public void getAllUserByEmail_ReturnTheListOfUserWithThisEmail() throws Exception{
		
		Account account=new Account("test123", 1000L, Currency.USD);
		User user=new User("crossover@crossover.com", "password", Role.USER, account);
		user.setUserId(1L);
		Account account2=new Account("test1234", 800L, Currency.USD);
		User user2=new User("crossover2@crossover.com", "password", Role.USER, account2);
		user2.setUserId(2L);
		Account account3=new Account("test123", 1000L, Currency.USD);
		User user3=new User("test@test.com", "password", Role.USER, account3);
		user3.setUserId(3L);
		List<User> listUser=new ArrayList<User>();
		listUser.add(user);
		listUser.add(user2);
		Page<User> pageUser=new PageImpl<>(listUser);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/users/get?email=crossover&page=0")
				.contentType(MediaType.APPLICATION_JSON);
		
		when(environment.getProperty("pages.number")).thenReturn("5");
		when(userService.getAllUserByEmail("crossover",new PageRequest(0, 5))).thenReturn(pageUser);

		//Now we can test the getAllUserMethod
		mockMvc.perform(requestBuilder)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.totalElements", is(2)))
		.andExpect(jsonPath("$.totalPages", is(1)))
		.andExpect(jsonPath("$.number", is(0)))
		.andExpect(jsonPath("$.content[0].email", is("crossover@crossover.com")))
		.andExpect(jsonPath("$.content[0].role", is(Role.USER.toString())))
		.andExpect(jsonPath("$.content[0].account.id", is("test123")))
		.andExpect(jsonPath("$.content[0].account.balance", is(1000)))
		.andExpect(jsonPath("$.content[0].account.currency", is(Currency.USD.toString())))
		.andExpect(jsonPath("$.content[1].email", is("crossover2@crossover.com")))
		.andExpect(jsonPath("$.content[1].role", is(Role.USER.toString())))
		.andExpect(jsonPath("$.content[1].account.id", is("test1234")))
		.andExpect(jsonPath("$.content[1].account.balance", is(800)))
		.andExpect(jsonPath("$.content[1].account.currency", is(Currency.USD.toString())));
	}

}
