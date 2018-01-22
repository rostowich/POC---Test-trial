package com.crossover.techtrial.java.se.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.crossover.techtrial.java.se.controllers.OfferController;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.objects.AirlineOffer;
import com.crossover.techtrial.java.se.objects.AirlineRoute;
import com.crossover.techtrial.java.se.objects.Balance;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.services.IExternalSystemService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class represents the unit test of the OfferControllerc class
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value=OfferController.class, secure=false)
@ActiveProfiles("test")
public class OfferControllerTest {

	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	//We  have to add all these mocks to the spring ApplicationContext
	@MockBean
	private IExternalSystemService externalSystemService;
	
	@Test
	public void getAllAvailableOffer_ReturnAirlineException() throws Exception{
		
		RequestBuilder requestBuilderForAvailableOffer = MockMvcRequestBuilders
				.get("/offers")
				.contentType(MediaType.APPLICATION_JSON);
		
		when(externalSystemService.getOffers()).thenThrow(new AirlineApiException());
		try{
			//Now we can getOffers method
			mockMvc.perform(requestBuilderForAvailableOffer).andReturn();
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(AirlineApiException.class)
			.hasMessage("Error when sending request to the external system");
		}
	}
	
	@Test
	public void getAllAvailableOffer_ReturnListOfAvailableOffer() throws Exception{
		
		AirlineOffer airlineOffer1=new AirlineOffer();
		airlineOffer1.setPrice(new Balance(100L, Currency.USD));
		airlineOffer1.setRoute(new AirlineRoute("London", "Madrid"));
		
		AirlineOffer airlineOffer2=new AirlineOffer();
		airlineOffer2.setPrice(new Balance(350L, Currency.EUR));
		airlineOffer2.setRoute(new AirlineRoute("Paris", "New york"));
		
		List<AirlineOffer> listOffers=new ArrayList<AirlineOffer>();
		listOffers.add(airlineOffer1);
		listOffers.add(airlineOffer2);
		
		RequestBuilder requestBuilderForAvailableOffers = MockMvcRequestBuilders
				.get("/offers")
				.contentType(MediaType.APPLICATION_JSON);
				
		when(externalSystemService.getOffers()).thenReturn(listOffers);
		
		//now we can test getOffers method
		mockMvc.perform(requestBuilderForAvailableOffers)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].route.from", is("London")))
		.andExpect(jsonPath("$[0].route.to", is("Madrid")))
		.andExpect(jsonPath("$[0].price.amount", is(100)))
		.andExpect(jsonPath("$[0].price.currency", is(Currency.USD.toString())))
		.andExpect(jsonPath("$[1].route.from", is("Paris")))
		.andExpect(jsonPath("$[1].route.to", is("New york")))
		.andExpect(jsonPath("$[1].price.amount", is(350)))
		.andExpect(jsonPath("$[1].price.currency", is(Currency.EUR.toString())));
		
	}
}
