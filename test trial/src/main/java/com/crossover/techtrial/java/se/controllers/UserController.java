package com.crossover.techtrial.java.se.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.exceptions.AirlineApiException;
import com.crossover.techtrial.java.se.exceptions.UserAlreadyExistException;
import com.crossover.techtrial.java.se.exceptions.UserNotFoundException;
import com.crossover.techtrial.java.se.objects.UserRegistration;
import com.crossover.techtrial.java.se.services.IUserService;
import com.crossover.techtrial.java.se.validators.RegistrationValidator;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private Environment environment;
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.setValidator(new RegistrationValidator());
	}

	@RequestMapping(value="/users/register", method=RequestMethod.POST)
	public User register(@Valid @RequestBody UserRegistration userRegistration) throws AirlineApiException, UserAlreadyExistException{

		logger.info("Create the user account in the system");
		User user=new User(userRegistration.getEmail(), userRegistration.getPassword());		
		return userService.register(user);
	}
	
	/**
	 * This method get all the information about the user
	 * @param userId
	 * @return
	 * @throws UserNotFoundException
	 */
	@RequestMapping(value="/users/{userId}", method=RequestMethod.GET)
	public User getAUser(@PathVariable Long userId) throws UserNotFoundException {
		Optional<User> user=userService.getUserById(userId);
		if(!user.isPresent())
			throw new UserNotFoundException();
		
		return user.get();
	}
	
	/**
	 * This method get all the users
	 * @param userId
	 * @return
	 */
	@RequestMapping(value="/users/all", method=RequestMethod.GET)
	public Page<User> getAllUser(int page)  {
		
		return userService.getAllUser(new PageRequest(page, Integer.parseInt(environment.getProperty("pages.number"))));
	}
	
	/**
	 * This method find all the user which contain email in their email address
	 * @param email
	 * @return
	 */
	@RequestMapping(value="/users/get", method=RequestMethod.GET)
	public Page<User> getAllUserByEmail(String email, int page){
		
		return userService.getAllUserByEmail(email, new PageRequest(page, Integer.parseInt(environment.getProperty("pages.number"))));
	}

}
