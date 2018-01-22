package com.crossover.techtrial.java.se.services;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crossover.techtrial.java.se.dao.IAccountDao;
import com.crossover.techtrial.java.se.dao.ITicketDao;
import com.crossover.techtrial.java.se.dao.IUserDao;
import com.crossover.techtrial.java.se.dao.TicketSpecification;
import com.crossover.techtrial.java.se.domain.Account;
import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.exceptions.InvalidArgumentException;
import com.crossover.techtrial.java.se.exceptions.NotEnoughAmountInAccountException;
import com.crossover.techtrial.java.se.exceptions.UserNotFoundException;
import com.crossover.techtrial.java.se.objects.Balance;

/**
 * This is the implementation for ITicketService
 * @author
 *
 */
@Service
public class TicketServiceImpl implements ITicketService{
	
	private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);
	
	@Autowired
	private ITicketDao ticketDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IExternalSystemService ExternalSystemService;
	
	@Autowired
	private IAccountDao accountDao;
	
	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.ITicketService#purchase(com.crossover.techtrial.java.se.domain.Ticket, java.lang.Long)
	 */
	@Override
	@Transactional
	public Ticket purchase(Ticket ticket, Long userId) throws UserNotFoundException, AirlineApiException, NotEnoughAmountInAccountException, InvalidArgumentException{
		
		if(ticket.getNumber()==0 || ticket.getNumber()!=(int)ticket.getNumber()){
			throw new InvalidArgumentException("The argument sent is not valid");
		}
		logger.info("1. Find the user in the database");
		Optional<User> user=userDao.findByUserId(userId);
		if(!user.isPresent()){
			throw new UserNotFoundException("User not found");
		}
		
		logger.info("2. Debit the user account in the database ");
		logger.info("2.1 Convert the amount to debit");
		Long totalAmount=ticket.getAmount()*ticket.getNumber();
		Balance monetaryAmount;
		try{
			monetaryAmount=ExternalSystemService.moneyExchange(totalAmount, ticket.getCurrency(), user.get().getAccount().getCurrency());	
		}
		catch(Exception e){
			logger.info("Error from API "+e.getMessage());
			throw new AirlineApiException("Error when converting currencies from the external system");
		}
		
		Long moneyToDebit=monetaryAmount.getAmount();
		logger.info("2.2 debit the account ");
		Account account=user.get().getAccount();
		if(account.getBalance()<moneyToDebit){
			//Throw the exception
			throw new NotEnoughAmountInAccountException("Not enough amount in the account");
		}
		account.setBalance(account.getBalance()-moneyToDebit);
		account=accountDao.save(account);
		
		logger.info("3. Withdraw the amount from the account from the external system ");
		try{
			ExternalSystemService.WithdrawMoneyFromAccountFromAPI(account.getId(), moneyToDebit, account.getCurrency());
		}
		catch(Exception e){
			logger.info("Error from API "+e.getMessage());
			throw new AirlineApiException("Not enough amount in the account from the external system");
		}

		logger.info("4. Save the ticket in the database");
		ticket.setDate(new Date());
		ticket.setUser(user.get());
		
		return ticketDao.save(ticket);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.ITicketService#findByUser(java.lang.Long, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Ticket> findByUser(Long userId, Pageable page) throws  UserNotFoundException {
		Optional<User> user=userDao.findByUserId(userId);
		if(!user.isPresent())
			throw new UserNotFoundException("User not found");
		return ticketDao.findByUser(user.get(), page);
	}

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.ITicketService#findAllByExample(com.crossover.techtrial.java.se.domain.Ticket, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<Ticket> findAllByExample(Ticket ticket, Pageable pageable) {
		Specification<Ticket> specification=new TicketSpecification(ticket);
		return ticketDao.findAll(specification, pageable);
	}

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.ITicketService#getTicket(java.lang.Long)
	 */
	@Override
	public Ticket getTicket(Long id) {
		return ticketDao.findOne(id);
	}
}
