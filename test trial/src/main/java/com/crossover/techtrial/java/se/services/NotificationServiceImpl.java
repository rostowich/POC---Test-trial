package com.crossover.techtrial.java.se.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.crossover.techtrial.java.se.exceptions.MailSendingException;

/**
 * This is the implementation for INotificationService
 * @author
 *
 */
@Service
public class NotificationServiceImpl implements INotificationService{

	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
	
	@Autowired
	private JavaMailSender javaMailSender;

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.INotificationService#sendHtml(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendHtml(String from, String personal,String recipient, String subject, String message) throws MailSendingException {		
		//we set isHtml parameter to true because we want to send html email.
		sendMail(from, personal,recipient, subject, message, true);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.INotificationService#sendPlainText(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void sendPlainText(String from, String personal,String recipient, String subject, String message) throws MailSendingException {		
		//we set isHtml parameter to true because we want to send plain text email.
		sendMail(from, personal,recipient, subject, message, false);
	}
	
	/**
	 * Method used to send the mail
	 * @param to
	 * @param subject
	 * @param message
	 * @param isHTML
	 * @throws MailSendingException
	 */
	private void sendMail(String from, String personal, String to, String subject, String message, Boolean isHTML) throws MailSendingException{
		try{
			//Prepare the message to send
			MimeMessagePreparator mail=new MimeMessagePreparator() {				
				@Override
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
					helper.setTo(to);
		            helper.setSubject(subject);
		            helper.setText(message, isHTML);
		            helper.setFrom(from, personal);
		            logger.info("Send email '{}' to: {}", subject, to);
				}
			}; 
            
            javaMailSender.send(mail);
		}
		catch(Exception e){
			logger.info(String.format("Problem with sending email "+ e.getMessage()));
			throw new MailSendingException("Error when sending mail");
		}
	}

}
