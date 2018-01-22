package com.crossover.techtrial.java.se.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.objects.AirlineOffer;
import com.crossover.techtrial.java.se.services.IExternalSystemService;

@RestController
public class OfferController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	IExternalSystemService externalSystemService;
	
	@RequestMapping(value="/offers", method=RequestMethod.GET)
	public List<AirlineOffer> getAllAvailableOffer() throws AirlineApiException {
		logger.info("Getting all the available offers");
		List<AirlineOffer> listOffer= externalSystemService.getOffers();
		return listOffer;
	}
}
