package com.crossover.techtrial.java.se.services;

import org.springframework.stereotype.Component;

import com.crossover.techtrial.java.se.exceptions.MailSendingException;

/**
 * This interface is in charge of notification. For this project we will implement 
 * Notification by email
 * @author 
 *
 */
@Component
public interface INotificationService {
	
	/**
	 * Send the html email
	 * @param from email of the site 
	 * @param personal name of the site
	 * @param recipient
	 * @param subject
	 * @param message
	 * @throws MailSendingException
	 */
	public void sendHtml(String from, String personal,String recipient, String subject, String message) throws MailSendingException;

	
	/**
	 * Send the plain text email
	 * @param from email of the site 
	 * @param personal name of the site
	 * @param recipient
	 * @param subject
	 * @param message
	 * @throws MailSendingException
	 */
	public void sendPlainText(String from, String personal,String recipient, String subject, String message) throws MailSendingException;

}
