package com.crossover.techtrial.java.se.services;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.IContext;

/**
 * We define this interface because the TemplateEngine class of thymeleaf dependencies provides process
 * method as a final method. And We are unable to mock final method while unit testing.
 * @author 
 *
 */
@Component
public interface ITemplateEngine {

	public String process(String template, IContext context);
}
