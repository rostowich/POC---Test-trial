package com.crossover.techtrial.java.se.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

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

import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.objects.Role;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/user-entries.xml")
@ActiveProfiles("test")
public class UserTestDao {

	@Autowired
	private IUserDao userDao;
	
	@After
	public void setup() {
        userDao.deleteAll();

	}
	
	@Test
 	public void findByEmail_EmailDoesNotExist_ShouldReturnEmptyObjet(){
 		Optional<User> user=userDao.findByEmail("notfoundemail@test.com");
 		assertFalse(user.isPresent());
 	}
	
	@Test
	public void findByEmail_EmailExist_ShouldReturnAUser(){
 		Optional<User> user=userDao.findByEmail("crossover@crossover.com");
 		assertTrue(user.isPresent());
 		assertThat(user.get()).isNotNull();
 		assertThat(user.get()).isExactlyInstanceOf(User.class);
 		assertThat(user.get().getEmail()).isEqualTo("crossover@crossover.com");
 		assertThat(user.get().getRole()).isEqualTo(Role.USER);
 	}
	
	@Test
 	public void findByUserId_IdDoesNotExist_ShouldReturnEmptyObjet(){
 		Optional<User> user=userDao.findByUserId(10L);
 		assertFalse(user.isPresent());
 	}
	
	@Test
	public void findByUserId_IdExist_ShouldReturnAUser(){
 		Optional<User> user=userDao.findByUserId(1L);
 		assertTrue(user.isPresent());
 		assertThat(user.get()).isNotNull();
 		assertThat(user.get()).isExactlyInstanceOf(User.class);
 		assertThat(user.get().getUserId()).isEqualTo(1L);
 		assertThat(user.get().getEmail()).isEqualTo("crossover@crossover.com");
 		assertThat(user.get().getRole()).isEqualTo(Role.USER);
 	}
	
	@Test
	public void findByEmailContaining_NotFound_ShouldNoUser(){
 		Page<User> user=userDao.findByEmailContaining("notFound", new PageRequest(0, 3));
 		assertThat(user.getSize()).isEqualTo(0);
 		assertThat(user.getTotalElements()).isEqualTo(0);
 		assertThat(user.getContent().isEmpty());
 	}
	
	@Test
	public void findByEmailContaining_EmailContain_ShouldPageOfUser(){
 		Page<User> user=userDao.findByEmailContaining("crossover", new PageRequest(0, 5));
 		assertThat(user.getContent()).isExactlyInstanceOf(User.class);
 		assertThat(user.getTotalPages()).isEqualTo(1);
 		assertThat(user.getContent().size()).isEqualTo(3);
 		assertThat(user.getNumberOfElements()).isEqualTo(3);
 	}
	
	

}
