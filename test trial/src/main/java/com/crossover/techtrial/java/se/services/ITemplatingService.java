package com.crossover.techtrial.java.se.services;

import org.springframework.stereotype.Component;

/**
 * This interface groups all the business operations concerning the templating
 * @author 
 *
 */
@Component
public interface ITemplatingService {

	/**
	 * This method return the html template 
	 * @param object
	 * @param templateName
	 * @return
	 */
	public String buildHtmlTemplating(Object object, String templateName);
}
