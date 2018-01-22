package com.crossover.techtrial.java.se.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.objects.UserLogin;
import com.crossover.techtrial.java.se.services.IUserService;

@RestController
public class LoginController {

private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	@Autowired
    private IUserService userService;
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	/**
	 * This method allow authentication through rest service
	 * @param userLogin
	 * @return
	 */
	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public ResponseEntity<User> login(@RequestBody UserLogin userLogin){
		
		logger.info("Login the user");
		UserDetails userDetails = userDetailsService.loadUserByUsername(userLogin.getEmail());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userLogin.getPassword(), userDetails.getAuthorities());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if(usernamePasswordAuthenticationToken.isAuthenticated()){
        	logger.info("The user has been logged in successfully");
        	SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        	Optional<User> user=userService.getUserByEmail(userDetails.getUsername());
        	return new ResponseEntity<>(user.get(),HttpStatus.OK);
        }
        logger.info("Authentication failure");
       	return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
