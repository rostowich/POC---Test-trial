package com.crossover.techtrial.java.se.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.crossover.techtrial.java.se.controllers.NotificationController;
import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.MailSendingException;
import com.crossover.techtrial.java.se.exceptions.TicketNotFoundException;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.services.INotificationService;
import com.crossover.techtrial.java.se.services.ITemplatingService;
import com.crossover.techtrial.java.se.services.ITicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class represents the unit test of the TicketController class
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value=NotificationController.class, secure=false)
@ActiveProfiles("test")
public class NotificationControllerTest {
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	//We  have to add all these mocks to the spring ApplicationContext
	@MockBean
	private INotificationService notificationService;
	
	@MockBean
	private Environment environment;
	
	@MockBean
	private ITicketService ticketService;
	
	@MockBean
	private ITemplatingService templatingService;
	
	@Test
	public void sendmail_ReturnTicketNotFoundException() throws Exception{
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/mail/1")
				.contentType(MediaType.APPLICATION_JSON);
		
		when(ticketService.getTicket(1L)).thenReturn(null);
		
		try{
			//Now we can getOffers method
			mockMvc.perform(requestBuilder).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(TicketNotFoundException.class)
			.hasMessage("Ticket not found");
		}
		}
	
	@Test
	public void sendmail_ReturnMailSendingException() throws Exception{
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/mail/1")
				.contentType(MediaType.APPLICATION_JSON);
		
		Ticket ticket=new Ticket("London", "Madrid", 100L, Currency.EUR, 2);
		ticket.setId(1L);
		User user=new User();
		user.setEmail("test@test.com");
		ticket.setUser(user);
		//Mock the getTicket method of the ticketService
		when(ticketService.getTicket(1L)).thenReturn(ticket);
		
        //We can mock the buildHtmlTemplating method of buildHtmlTemplating bean
		when(templatingService.buildHtmlTemplating(ticket, "mailTemplate")).thenReturn("<p>Html template</p>");
		
		//We can mock the sendHtml method of notificationServiceBean
		when(environment.getProperty("spring.mail.username")).thenReturn("crossover@crossover.com");
		when(environment.getProperty("mail.site.name")).thenReturn("crossover");
		when(environment.getProperty("mail.subject")).thenReturn("Test subject");
		doThrow(MailSendingException.class).when(notificationService).sendHtml("crossover@crossover.com", "crossover", "test@test.com", "Test subject", "<p>Html template</p>");
		
		//Now can test the method
		try{
			mockMvc.perform(requestBuilder).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(MailSendingException.class)
			.hasMessage("Error when sending the mail");
		}
	}
	
	@Test
	public void sendmail_SendingMail() throws Exception{
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/mail/1")
				.contentType(MediaType.APPLICATION_JSON);
		
		Ticket ticket=new Ticket("London", "Madrid", 100L, Currency.EUR, 2);
		ticket.setId(1L);
		User user=new User();
		user.setEmail("test@test.com");
		ticket.setUser(user);
		//Mock the getTicket method of the ticketService
		when(ticketService.getTicket(1L)).thenReturn(ticket);
		
        //We can mock the buildHtmlTemplating method of buildHtmlTemplating bean
		when(templatingService.buildHtmlTemplating(ticket, "mailTemplate")).thenReturn("<p>Html template</p>");
		
		//We can mock the sendHtml method of notificationServiceBean
		when(environment.getProperty("spring.mail.username")).thenReturn("crossover@crossover.com");
		when(environment.getProperty("mail.site.name")).thenReturn("crossover");
		when(environment.getProperty("mail.subject")).thenReturn("Test subject");
		doNothing().when(notificationService).sendHtml("crossover@crossover.com", "crossover", "test@test.com", "Test subject", "<p>Html template</p>");
		
		//Now can test the method
		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", is("<p>Html template</p>")));
		
		verify(notificationService, times(1)).sendHtml("crossover@crossover.com", "crossover", "test@test.com", "Test subject", "<p>Html template</p>");
	}
}
