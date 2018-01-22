package com.crossover.techtrial.java.se.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.java.se.dao.IAccountDao;
import com.crossover.techtrial.java.se.domain.Account;

/**
 * This is the implementation for IAccountService
 * @author
 *
 */
@Service
public class  AccountServiceImpl implements IAccountService{
	
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
		
	@Autowired
	private IAccountDao accountDao;

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IAccountService#save(com.crossover.techtrial.java.se.domain.Account)
	 */
	@Override
	public Account save(Account account) {
		logger.info("Saving the account in the database");
		return accountDao.save(account);
	}
}
