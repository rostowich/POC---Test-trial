package com.crossover.techtrial.java.se.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import com.crossover.techtrial.java.se.controllers.TicketController;
import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.exceptions.InvalidArgumentException;
import com.crossover.techtrial.java.se.exceptions.NotEnoughAmountInAccountException;
import com.crossover.techtrial.java.se.exceptions.UserNotFoundException;
import com.crossover.techtrial.java.se.objects.AirlineOffer;
import com.crossover.techtrial.java.se.objects.AirlineRoute;
import com.crossover.techtrial.java.se.objects.Balance;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.objects.PurchaseTicket;
import com.crossover.techtrial.java.se.objects.Role;
import com.crossover.techtrial.java.se.services.ITicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class represents the unit test of the TicketController class
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value=TicketController.class, secure=false)
@ActiveProfiles("test")
public class TicketControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	//We  have to add all these mocks to the spring ApplicationContext
	@MockBean
	private ITicketService ticketService;
	
	@MockBean
	private Environment environment;
	
	@Test
	public void getAllTicketForAUser_ReturnUserNotFoundException() throws Exception{
		
		RequestBuilder requestBuilderForGettingUserTicket = MockMvcRequestBuilders
				.get("/tickets/user/1?page=0")
				.contentType(MediaType.APPLICATION_JSON);
		
		when(environment.getProperty("pages.number")).thenReturn("5");
		when(ticketService.findByUser(1L, new PageRequest(0, 5))).thenThrow(new UserNotFoundException());
		
		try{
			//Now we can getOffers method
			mockMvc.perform(requestBuilderForGettingUserTicket).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(UserNotFoundException.class)
			.hasMessage("User not found");
		}
		}
	
	@Test
	public void getAllTicketForAUser_ReturnListOfUserTickets() throws Exception{
		
		Ticket ticket1=new Ticket("London", "Paris", 100L, Currency.EUR, 1);
		Ticket ticket2=new Ticket("London", "Madrid", 150L, Currency.EUR, 2);
		User user=new User();
		user.setUserId(1L);
		ticket1.setUser(user);
		ticket2.setUser(user);
		List<Ticket> listTicket=new ArrayList<Ticket>();
		listTicket.add(ticket1);
		listTicket.add(ticket2);
		Page<Ticket> pageTicket=new PageImpl<>(listTicket);
		
		RequestBuilder requestBuilderForGettingUserTicket = MockMvcRequestBuilders
				.get("/tickets/user/1?page=0")
				.contentType(MediaType.APPLICATION_JSON);
		
		when(environment.getProperty("pages.number")).thenReturn("5");
		when(ticketService.findByUser(1L, new PageRequest(0, 5))).thenReturn(pageTicket);
		
		//Now we can test the getAllTicketForAUser method
		mockMvc.perform(requestBuilderForGettingUserTicket)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalElements", is(2)))
			.andExpect(jsonPath("$.totalPages", is(1)))
			.andExpect(jsonPath("$.number", is(0)))
			.andExpect(jsonPath("$.content[0].departure", is("London")))
			.andExpect(jsonPath("$.content[0].destination", is("Paris")))
			.andExpect(jsonPath("$.content[0].amount", is(100)))
			.andExpect(jsonPath("$.content[0].currency", is(Currency.EUR.toString())))
			.andExpect(jsonPath("$.content[0].number", is(1)))
			.andExpect(jsonPath("$.content[1].departure", is("London")))
			.andExpect(jsonPath("$.content[1].destination", is("Madrid")))
			.andExpect(jsonPath("$.content[1].amount", is(150)))
			.andExpect(jsonPath("$.content[1].currency", is(Currency.EUR.toString())))
			.andExpect(jsonPath("$.content[1].number", is(2)));
		}

	@Test
	public void getAllByExample_ReturnListOfAllTickets() throws Exception{
		
		Ticket ticket1=new Ticket("London", "Madrid", 100L, Currency.EUR, 1);
		Ticket ticket2=new Ticket("London", "Madrid", 100L, Currency.EUR, 2);
		List<Ticket> listTicket=new ArrayList<Ticket>();
		listTicket.add(ticket1);
		listTicket.add(ticket2);
		Page<Ticket> pageTicket=new PageImpl<>(listTicket);
		
		RequestBuilder requestBuilderForGettingAllTicket = MockMvcRequestBuilders
				.get("/tickets/all?departure=London&destination=Madrid&amount=100&page=0")
				.contentType(MediaType.APPLICATION_JSON);
		
		Ticket ticketExample=new Ticket();
		ticketExample.setDestination("Madrid");
		ticketExample.setDeparture("London");
		ticketExample.setAmount(100L);
		
		when(environment.getProperty("pages.number")).thenReturn("5");
		when(ticketService.findAllByExample(refEq(ticketExample,"id","number","date","beginDate", "endDate","user"), refEq(new PageRequest(0, 5)))).thenReturn(pageTicket);
		
		//Now we can test the getAll method
		mockMvc.perform(requestBuilderForGettingAllTicket)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.totalElements", is(2)))
			.andExpect(jsonPath("$.totalPages", is(1)))
			.andExpect(jsonPath("$.number", is(0)))
			.andExpect(jsonPath("$.content[0].departure", is("London")))
			.andExpect(jsonPath("$.content[0].destination", is("Madrid")))
			.andExpect(jsonPath("$.content[0].amount", is(100)))
			.andExpect(jsonPath("$.content[0].currency", is(Currency.EUR.toString())))
			.andExpect(jsonPath("$.content[0].number", is(1)))
			.andExpect(jsonPath("$.content[1].departure", is("London")))
			.andExpect(jsonPath("$.content[1].destination", is("Madrid")))
			.andExpect(jsonPath("$.content[1].amount", is(100)))
			.andExpect(jsonPath("$.content[1].currency", is(Currency.EUR.toString())))
			.andExpect(jsonPath("$.content[1].number", is(2)));
		}
	
	@Test
	public void findById_ReturnTicketById() throws Exception{
		
		Ticket ticket1=new Ticket("London", "Paris", 100L, Currency.EUR, 1);
		ticket1.setId(1L);
		
		RequestBuilder requestBuilderForGettingTicketById = MockMvcRequestBuilders
				.get("/tickets/1")
				.contentType(MediaType.APPLICATION_JSON);
		
		RequestBuilder requestBuilderForNotFoundTicket = MockMvcRequestBuilders
				.get("/tickets/2")
				.contentType(MediaType.APPLICATION_JSON);
		
		when(ticketService.getTicket(1L)).thenReturn(ticket1);
		
		when(ticketService.getTicket(2L)).thenReturn(null);
		
		//Now we can test the method which will return a ticket 
		mockMvc.perform(requestBuilderForGettingTicketById)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.departure", is("London")))
			.andExpect(jsonPath("$.destination", is("Paris")))
			.andExpect(jsonPath("$.amount", is(100)))
			.andExpect(jsonPath("$.currency", is(Currency.EUR.toString())))
			.andExpect(jsonPath("$.number", is(1)));
		
		//Now we can test the method which will return null
		MvcResult resultGetTicketById=mockMvc.perform(requestBuilderForNotFoundTicket)
		.andExpect(status().isOk())
		.andReturn();
		
		MockHttpServletResponse response=resultGetTicketById.getResponse();
		assertThat(response.getContentAsString()).isEmpty();
		}
	@Test
	public void purchaseTicket_ThrowInvalidArgumentException() throws Exception{
		
		PurchaseTicket purchaseTicket=new PurchaseTicket();
		purchaseTicket.setUserId(1L);
		AirlineOffer offer=new AirlineOffer(new Balance(1000L, Currency.USD), new AirlineRoute("London", "Madrid"));
		purchaseTicket.setOffer(offer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/tickets/user/purchase")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(purchaseTicket))
				.contentType(MediaType.APPLICATION_JSON);
		
		when(ticketService.purchase(any(Ticket.class), anyLong())).thenThrow(new InvalidArgumentException());
		try{
			//Now we can getUserById method
			mockMvc.perform(requestBuilder).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(InvalidArgumentException.class)
			.hasMessage("The argument sent is not valid");
		}		
	}
	
	@Test
	public void purchaseTicket_ThrowUserNotFoundException() throws Exception{
		
		PurchaseTicket purchaseTicket=new PurchaseTicket();
		purchaseTicket.setNumber(1);
		purchaseTicket.setUserId(1L);
		AirlineOffer offer=new AirlineOffer(new Balance(1000L, Currency.USD), new AirlineRoute("London", "Madrid"));
		purchaseTicket.setOffer(offer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/tickets/user/purchase")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(purchaseTicket))
				.contentType(MediaType.APPLICATION_JSON);
		
		when(ticketService.purchase(any(Ticket.class), anyLong())).thenThrow(new UserNotFoundException());
		try{
			//Now we can getUserById method
			mockMvc.perform(requestBuilder).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(UserNotFoundException.class)
			.hasMessage("User not found");
		}		
	}
	
	@Test
	public void purchaseTicket_ThrowAirlineApiException() throws Exception{

		PurchaseTicket purchaseTicket=new PurchaseTicket();
		purchaseTicket.setNumber(1);
		purchaseTicket.setUserId(1L);
		AirlineOffer offer=new AirlineOffer(new Balance(1000L, Currency.USD), new AirlineRoute("London", "Madrid"));
		purchaseTicket.setOffer(offer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/tickets/user/purchase")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(purchaseTicket))
				.contentType(MediaType.APPLICATION_JSON);
		
		when(ticketService.purchase(any(Ticket.class), anyLong())).thenThrow(new AirlineApiException());
		try{
			//Now we can getUserById method
			mockMvc.perform(requestBuilder).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(AirlineApiException.class)
			.hasMessage("Error when sending request to the external system");
		}		
	}
	
	@Test
	public void purchaseTicket_ThrowNotEnoughAmountInAccountException() throws Exception{
		
		PurchaseTicket purchaseTicket=new PurchaseTicket();
		purchaseTicket.setNumber(1);
		purchaseTicket.setUserId(1L);
		AirlineOffer offer=new AirlineOffer(new Balance(1000L, Currency.USD), new AirlineRoute("London", "Madrid"));
		purchaseTicket.setOffer(offer);
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/tickets/user/purchase")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(purchaseTicket))
				.contentType(MediaType.APPLICATION_JSON);
		
		when(ticketService.purchase(any(Ticket.class), anyLong())).thenThrow(new NotEnoughAmountInAccountException());
		try{
			//Now we can getUserById method
			mockMvc.perform(requestBuilder).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(NotEnoughAmountInAccountException.class)
			.hasMessage("Not enough amount in the account");
		}		
	}

	@Test
	public void purchaseTicket_WhichSaveTheTicket() throws Exception{
		//Create the PurchaseTicketObjet
		PurchaseTicket purchaseTicket=new PurchaseTicket();
		purchaseTicket.setNumber(1);
		purchaseTicket.setUserId(1L);
		AirlineOffer offer=new AirlineOffer(new Balance(100L, Currency.EUR), new AirlineRoute("London", "Madrid"));
		purchaseTicket.setOffer(offer);
		purchaseTicket.setUserId(1L);
		
		User user=new User("crossover@crossover.com", "password", Role.USER);
		user.setUserId(1L);

		Ticket ticket=new Ticket("London", "Madrid", 100L, Currency.EUR, 1);
		Ticket ticketSaved=new Ticket("London", "Madrid", 100L, Currency.EUR, 1);
		ticketSaved.setUser(user);
		
		//Mock the purchase method of TicketService
		when(ticketService.purchase(refEq(ticket, "id","user","date"), eq(1L))).thenReturn(ticketSaved);
				
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/tickets/user/purchase")
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(purchaseTicket))
				.contentType(MediaType.APPLICATION_JSON);

		//Now we can perform the method
		mockMvc.perform(requestBuilder)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.amount", is(100)))
		.andExpect(jsonPath("$.currency", is("EUR")))
		.andExpect(jsonPath("$.departure", is("London")))
		.andExpect(jsonPath("$.destination", is("Madrid")))
		.andExpect(jsonPath("$.number", is(1)))
		.andExpect(jsonPath("$.user.userId", is(1)));	
	}
}
