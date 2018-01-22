package com.crossover.techtrial.java.se.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring4.SpringTemplateEngine;

/**
 * This class is a wrapper of the Original TemplateEngine Class provided by the Thymeleaf dependency. 
 * Because the process method of this class is final, we are unable to mock it while unit testing, so that 
 * we have created this class to allow mocking the process method
 * @author 
 *
 */
@Component
public class TemplateEngineWrapper implements ITemplateEngine{

	@Autowired
    private SpringTemplateEngine templateEngine;

	@Override
	public String process(String templateName, IContext context) {
		return templateEngine.process(templateName, context);
	}

}
