package com.crossover.techtrial.java.se.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.exceptions.MailSendingException;
import com.crossover.techtrial.java.se.exceptions.TicketNotFoundException;
import com.crossover.techtrial.java.se.services.INotificationService;
import com.crossover.techtrial.java.se.services.ITemplatingService;
import com.crossover.techtrial.java.se.services.ITicketService;

@RestController
public class NotificationController {

	private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
	
	/**
	 * Name of the template for email
	 */
	private final String MAIL_TEMPLATE="mailTemplate";
	
	@Autowired
	private INotificationService notificationService;
	
	@Autowired
	private ITemplatingService templatingService;

	@Autowired
	private Environment environment;
	
	@Autowired
	private ITicketService ticketService;
	
	/**
	 * Send the mail
	 * @param id
	 * @return
	 * @throws MailSendingException 
	 * @throws TicketNotFoundException 
	 */
	@RequestMapping(value="/mail/{ticketId}", method=RequestMethod.GET)
	public ResponseEntity<String> sendMail(@PathVariable Long ticketId) throws MailSendingException, TicketNotFoundException {
			logger.info("Getting the ticket");
			Ticket ticket=ticketService.getTicket(ticketId);
			if(ticket==null)
				throw new TicketNotFoundException();
			
			//Build the template
			String htmlTemplate=templatingService.buildHtmlTemplating(ticket, this.MAIL_TEMPLATE);
	        
	        //Send the mail
	        notificationService.sendHtml(environment.getProperty("spring.mail.username"),environment.getProperty("mail.site.name"), ticket.getUser().getEmail(), environment.getProperty("mail.subject"), htmlTemplate);
	        return new ResponseEntity<String>(htmlTemplate,HttpStatus.OK);
	}
}
