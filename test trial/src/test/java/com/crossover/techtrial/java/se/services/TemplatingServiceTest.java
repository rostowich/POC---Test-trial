package com.crossover.techtrial.java.se.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.context.Context;

import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.objects.Currency;

/**
 * This class is used to unit test the TemplateService class
 * @author
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class TemplatingServiceTest {

	//We will mock the abstract method and any method calling web services of external
	//system for unit tests
	@InjectMocks
	private TicketTemplatingService ticketTemplatingService;
	
	@Mock
	private ITemplateEngine templateEngine;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testHtmlTemplateReturnTheHtmlTemplate(){
		Ticket ticket= new Ticket("London", "Madrid", 100L, Currency.EUR, 2);
		ticket.setId(1L);
		Date date=new Date();
		ticket.setDate(date);
		User user=new User();
		user.setEmail("crossover@crossover.com");
		ticket.setUser(user);
		
		Context context=new Context();
		context.setVariable("id", 1L);
        context.setVariable("date", date);
        context.setVariable("departure", "London");
        context.setVariable("destination", "Madrid");
        context.setVariable("amount", 100L);
        context.setVariable("number", 2);
        context.setVariable("currency", "EUR");
        context.setVariable("email", "crossover@crossover.com");
		//Mock the process method of the templateEngine bean
		when(templateEngine.process(refEq("TemplateName"), refEq(context))).thenReturn("Html template result");
		
		//Now we can test the method
		String htmlTemplate=ticketTemplatingService.buildHtmlTemplating(ticket, "TemplateName");
		assertThat(htmlTemplate).isEqualTo("Html template result");
	}
}
