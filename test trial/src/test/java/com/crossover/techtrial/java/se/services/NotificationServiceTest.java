package com.crossover.techtrial.java.se.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.techtrial.java.se.exceptions.MailSendingException;
import com.icegreen.greenmail.mail.MailException;

/**
 * This class is used to unit test NotificationService
 * @author
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class NotificationServiceTest {

	//We will mock the abstract method and any method calling web services of external
	//system for unit tests
	
	@InjectMocks
	private NotificationServiceImpl notificationService;

	@Mock
	private JavaMailSender javaMailSender;
    
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void sendHtmlTest_ReturnMailSendingExecption(){
		//Mock the send method of javaMailSender bean
		doThrow(MailException.class).when(javaMailSender).send(any(MimeMessagePreparator.class));
		
		//Now we can try to call the method
		try{
			notificationService.sendHtml("test@test.com", "Airline Portal", "recipient@recip.com", "Subject test", "Contain test");
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(MailSendingException.class)
			.hasMessage("Error when sending mail");
		}
	}
	
	@Test
	public void sendHtmlTest_Sendmail() throws MessagingException, MailSendingException{
		
		MimeMessagePreparator mailPreparator=new MimeMessagePreparator() {				
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
				helper.setTo("recipient@recip.com");
	            helper.setSubject("Subject test");
	            helper.setText("Contain test", true);
	            helper.setFrom("test@test.com", "Airline Portal");
			}
		}; 
		//Mock the send method of javaMailSender bean
		doNothing().when(javaMailSender).send(mailPreparator);
		//Now we can test the message 
		notificationService.sendHtml("test@test.com", "Airline Portal", "recipient@recip.com", "Subject test", "Contain test");
		
		verify(javaMailSender, times(1)).send(any(MimeMessagePreparator.class));
	}
	
}
