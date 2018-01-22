package com.crossover.techtrial.java.se.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.crossover.techtrial.java.se.domain.Ticket;

/**
 * This is the implementation for ITemplatingService
 * @author
 *
 */
@Service
public class TicketTemplatingService implements ITemplatingService{
	
	@Autowired
	private ITemplateEngine templateEngine;
	
	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.ITemplatingService#buildHtmlTemplating(java.lang.Object, java.lang.String)
	 */
	@Override
	public String buildHtmlTemplating(Object object, String templateName) {
		Ticket ticket= (Ticket)object;
		//Build the template
		Context context = new Context();
        context.setVariable("id", ticket.getId());
        context.setVariable("date", ticket.getDate());
        context.setVariable("departure", ticket.getDeparture());
        context.setVariable("destination", ticket.getDestination());
        context.setVariable("amount", ticket.getAmount());
        context.setVariable("number", ticket.getNumber());
        context.setVariable("currency", ticket.getCurrency().toString());
        context.setVariable("email", ticket.getUser().getEmail());
		String result=templateEngine.process(templateName, context);
		
		return result;
	}

}
