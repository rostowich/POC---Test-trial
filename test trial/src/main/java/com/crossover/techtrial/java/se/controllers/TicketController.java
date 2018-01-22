package com.crossover.techtrial.java.se.controllers;



import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.exceptions.InvalidArgumentException;
import com.crossover.techtrial.java.se.exceptions.NotEnoughAmountInAccountException;
import com.crossover.techtrial.java.se.exceptions.UserNotFoundException;
import com.crossover.techtrial.java.se.objects.PurchaseTicket;
import com.crossover.techtrial.java.se.services.ITicketService;

@RestController
public class TicketController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private ITicketService ticketService;
	
	@Autowired
	private Environment environment;

	/**
	 * Perform the purchase of a ticket
	 * @param purchaseTicket
	 * @return
	 * @throws UserNotFoundException
	 * @throws AirlineApiException
	 * @throws NotEnoughAmountInAccountException
	 * @throws InvalidArgumentException 
	 */
	@RequestMapping(value="/tickets/user/purchase", method=RequestMethod.POST)
	public Ticket purchaseTicket(@RequestBody PurchaseTicket purchaseTicket) throws UserNotFoundException, AirlineApiException, NotEnoughAmountInAccountException, InvalidArgumentException{
		logger.info("Purchasing the ticket ");
		Ticket ticket=new Ticket();
		ticket.setAmount(purchaseTicket.getOffer().getPrice().getAmount());
		ticket.setCurrency(purchaseTicket.getOffer().getPrice().getCurrency());
		ticket.setDeparture(purchaseTicket.getOffer().getRoute().getFrom());
		ticket.setDestination(purchaseTicket.getOffer().getRoute().getTo());
		ticket.setNumber(purchaseTicket.getNumber());
		return ticketService.purchase(ticket, purchaseTicket.getUserId());
	}

	/**
	 * Get the ticket by its ID
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/tickets/{id}", method=RequestMethod.GET)
	public Ticket findById(@PathVariable Long id) {
		return ticketService.getTicket(id);
	}

	/**
	 * get all the tickets 
	 * @param page
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping(value="/tickets/all", method=RequestMethod.GET)
	public Page<Ticket> getAllByExample(Long id, String departure, String destination, Long amount, 
								Integer number, String email, String beginDate, String endDate, int page) throws ParseException {
		
		Ticket ticket=new Ticket();
		ticket.setId(id);
		ticket.setDeparture(departure);
		ticket.setDestination(destination);
		ticket.setAmount(amount);
		ticket.setNumber(number);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(beginDate!=null)
		ticket.setBeginDate(formatter.parse(beginDate+" 00:00:00"));
		if(endDate!=null)
		ticket.setEndDate(formatter.parse(endDate+" 23:59:59"));
		User user=new User();
		user.setEmail(email);
		ticket.setUser(user);
				
		return ticketService.findAllByExample(ticket, new PageRequest(page, Integer.parseInt(environment.getProperty("pages.number"))));
	}
	
	/**
	 * This method get the tickets for a specific user
	 * @param userId
	 * @param page
	 * @return
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value="/tickets/user/{userId}", method=RequestMethod.GET)
	public Page<Ticket> getAllTicketForAUser(@PathVariable Long userId, int page) throws UserNotFoundException {
		return ticketService.findByUser(userId, new PageRequest(page, Integer.parseInt(environment.getProperty("pages.number"))));
	}

}
