package com.crossover.techtrial.java.se.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crossover.techtrial.java.se.dao.IAccountDao;
import com.crossover.techtrial.java.se.dao.IUserDao;
import com.crossover.techtrial.java.se.domain.Account;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.exceptions.UserAlreadyExistException;
import com.crossover.techtrial.java.se.objects.AccountResponse;
import com.crossover.techtrial.java.se.objects.Balance;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.objects.Role;

/**
 * This is the implementation for IUserService
 * @author 
 *
 */
@Service
public class UserServiceImpl implements IUserService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IAccountDao accountDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private IExternalSystemService utilityService;
	
	@Autowired
	private Environment environment;
	
	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IUserService#register(com.crossover.techtrial.java.se.domain.User)
	 */
	@Override
	@Transactional
	public User register(User user) throws UserAlreadyExistException, AirlineApiException {	
		
		logger.info("1.Find the user: if it already exists a user with the email, throw the exception");
		Optional<User> opUser=userDao.findByEmail(user.getEmail());
		if(opUser.isPresent())
			throw new UserAlreadyExistException("There is already a user with this email address");
		
		logger.info("2. Create the account from External System");
		Currency initialCurrency=Currency.valueOf(environment.getProperty("initial.accountCurency"));
		AccountResponse accountResponse;
		try{
			accountResponse=utilityService.getAccountFromAPI(initialCurrency);
		}
		catch(Exception e){
			logger.info("Error from API "+e.getMessage());
			throw new AirlineApiException("Error when creating the account from the external system");
		}

		logger.info("3. Convert the initial 1000 USD to the account currency");
		Long initialAmount=Long.parseLong(environment.getProperty("initial.amount"),10);
		Balance monetaryAmount;
		try{
			monetaryAmount=utilityService.moneyExchange(initialAmount, initialCurrency, accountResponse.getBalance().getCurrency());
		}
		catch(Exception e){
			logger.info("Error from API "+e.getMessage());
			throw new AirlineApiException("Error when converting currencies from the external system");
		}
		logger.info("4. Credit the account from the external system with the initial amount");
		AccountResponse depositAccountResponse;
		try{
			depositAccountResponse=utilityService.creditAccountFromAPI(accountResponse.getId(), monetaryAmount.getAmount(), accountResponse.getBalance().getCurrency());
		}
		catch(Exception e){
			logger.info("Error from API "+e.getMessage());
			throw new AirlineApiException("Error when crediting the account from the external system");
		}
		
		logger.info("5. Save the account in the database");
		Account account=new Account();
		account.setId(depositAccountResponse.getId());
		account.setBalance(depositAccountResponse.getBalance().getAmount());
		account.setCurrency(depositAccountResponse.getBalance().getCurrency());
		account=accountDao.save(account);
		
		logger.info("6. Create the user in the database");
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(Role.USER);
		user.setAccount(account);
		
		return userDao.save(user);
	}

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IUserService#getUserByEmail(java.lang.String)
	 */
	@Override
	public Optional<User> getUserByEmail(String email) {
		return userDao.findByEmail(email);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IUserService#getUserById(java.lang.Long)
	 */
	@Override
	public Optional<User> getUserById(Long id) {
		return userDao.findByUserId(id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IUserService#getAllUserByEmail(java.lang.String, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<User> getAllUserByEmail(String email, Pageable pageRequest) {
		return userDao.findByEmailContaining(email, pageRequest);
	}

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IUserService#getAllUser(org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<User> getAllUser(Pageable pageRequest) {
		return userDao.findAll(pageRequest);
	}

}
