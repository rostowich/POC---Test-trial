package com.crossover.techtrial.java.se.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * We need a custom authenticationEntryPoint because default Spring-Security config will redirect to login page. 
 * In our case we need just a http status 401 and a json response.
 *
 */
@Component
public class RESTAuthenticationEntryPoint implements AuthenticationEntryPoint{
	
	/**
	 * This code simply responds with a 401 Unauthorized status code as 
	 * soon as there’s an authentication problem
	 */
	@Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

}
