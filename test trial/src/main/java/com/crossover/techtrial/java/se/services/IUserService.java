package com.crossover.techtrial.java.se.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.exceptions.UserAlreadyExistException;

/**
 * This interface groups all the business operations concerning the users
 * @author 
 *
 */
@Component
public interface IUserService {

	/**
	 * This method registers a new user in the system
	 * @param user
	 * @return
	 * @throws UserAlreadyExistException
	 * @throws AirlineApiException
	 */
	public User register(User user) throws UserAlreadyExistException, AirlineApiException;
	
	/**
	 * This method find a user by his id
	 * @param email
	 * @return
	 */
	public Optional<User> getUserByEmail(String email);
	
	/**
	 * This method find all the users containing the email param in his email
	 * @param email
	 * @param pageRequest
	 * @return
	 */
	public Page<User> getAllUserByEmail(String email, Pageable pageRequest);
	
	/**
	 * This method find all the user of the system
	 * @param pageRequest
	 * @return
	 */
	public Page<User> getAllUser(Pageable pageRequest);
	
	/**
	 * This method find a user by his id
	 * @param id
	 * @return
	 */
	public Optional<User> getUserById(Long id);
	
}
