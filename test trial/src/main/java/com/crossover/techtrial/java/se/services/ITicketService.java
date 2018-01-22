package com.crossover.techtrial.java.se.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.exceptions.InvalidArgumentException;
import com.crossover.techtrial.java.se.exceptions.NotEnoughAmountInAccountException;
import com.crossover.techtrial.java.se.exceptions.UserNotFoundException;

/**
 * This interface groups all the business operations concerning the tickets
 * @author 
 *
 */
@Component
public interface ITicketService {
	
	/**
	 * This method is used to purchase a ticket
	 * @param ticket
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 * @throws AirlineApiException
	 * @throws NotEnoughAmountInAccountException
	 */
	public Ticket purchase(Ticket ticket, Long userId) throws UserNotFoundException, AirlineApiException, NotEnoughAmountInAccountException,InvalidArgumentException;
	
	/**
	 * This method is used to find the  tickets for a user
	 * @param userId
	 * @param pageable
	 * @return
	 * @throws UserNotFoundException
	 */
	public Page<Ticket> findByUser(Long userId, Pageable pageable) throws  UserNotFoundException;
	
	/**
	 * This method is used to find the tickets by a specific criteria
	 * @param ticket
	 * @param pageable
	 * @return
	 */
	public Page<Ticket> findAllByExample(Ticket ticket, Pageable pageable);
	
	/**
	 * This method id used to get a ticket by its id
	 * @param id
	 * @return
	 */
	public Ticket getTicket(Long id);
	
}
