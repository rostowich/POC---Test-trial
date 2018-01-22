package com.crossover.techtrial.java.se.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.domain.User;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/ticket-entries.xml")
@ActiveProfiles("test")
public class TicketDaoTest {

	@Autowired
	private ITicketDao ticketDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IAccountDao accountDao;
	
	@After
	public void setup() {
		ticketDao.deleteAll();
		userDao.deleteAll();
		accountDao.deleteAll();

	}
	
	@Test
 	public void findAll(){
 		Page<Ticket> ticket=ticketDao.findAll(new PageRequest(0, 3));
 		assertThat(ticket.getTotalElements()).isEqualTo(4);
 		assertThat(ticket.getContent().size()).isEqualTo(3);
 		
 		Page<Ticket> ticket2=ticketDao.findAll(new PageRequest(0, 5));
 		assertThat(ticket2.getTotalElements()).isEqualTo(4);
 		assertThat(ticket2.getContent().size()).isEqualTo(4);
 	}
	
	@Test
	public void findByUser_UserDoesNotExist_ShouldReturnEmpty(){
		User user=new User();
		user.setUserId(new Long(4));
		
		Page<Ticket> ticket=ticketDao.findByUser(user, new PageRequest(0, 5));
		assertThat(ticket.getTotalElements()).isEqualTo(0);
 		assertThat(ticket.getContent().size()).isEqualTo(0);
	}
	
	@Test
	public void findByUser_UserExists_ShouldReturnPageOfUsers(){
		User user=new User();
		user.setUserId(new Long(1));
		
		Page<Ticket> ticket=ticketDao.findByUser(user, new PageRequest(0, 5));
		assertThat(ticket.getTotalElements()).isEqualTo(3);
 		assertThat(ticket.getContent().size()).isEqualTo(3);
		
	}
	
}
