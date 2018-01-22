package com.crossover.techtrial.java.se.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.crossover.techtrial.java.se.controllers.ExportController;
import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.TicketNotFoundException;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.services.IGenerateFileService;
import com.crossover.techtrial.java.se.services.ITemplatingService;
import com.crossover.techtrial.java.se.services.ITicketService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class is used to unit test the ExportController class
 * @author mdr
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value=ExportController.class, secure=false)
@ActiveProfiles("test")
public class ExportControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
	private IGenerateFileService generateFileService;
	
	@MockBean
	private ITicketService ticketService;
	
	@MockBean
	private ITemplatingService templatingService;
	
	@Test
	public void downloadPdf_ReturnTicketNotFoundException() throws Exception{
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/pdf/1")
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
	public void downloadPdf_ReturnTheTicket() throws Exception{
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/pdf/1")
				.contentType(MediaType.APPLICATION_JSON);
		
		Ticket ticket=new Ticket("London", "Madrid", 100L, Currency.EUR, 2);
		ticket.setId(1L);
		User user=new User();
		user.setEmail("test@test.com");
		ticket.setUser(user);
		
		//We can mock the getTicket method of ticketService bean
		when(ticketService.getTicket(1L)).thenReturn(ticket);
		
		//We can mock the buildHtmlTemplating method of templatingService bean
		when(templatingService.buildHtmlTemplating(ticket, "pdfTemplate")).thenReturn("<p>Html template test</p>");
		File tmpFile=File.createTempFile("test", ".pdf");
		when(generateFileService.generateFile("<p>Html template test</p>")).thenReturn(tmpFile);
		
		//Now we can getOffers method
		mockMvc.perform(requestBuilder).andExpect(status().isOk());	
		assertThat(tmpFile.exists());
		assertThat(tmpFile.isFile());
		assertThat(tmpFile.createNewFile());
		}
}
