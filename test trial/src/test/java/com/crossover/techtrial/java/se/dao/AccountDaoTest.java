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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.crossover.techtrial.java.se.domain.Account;
import com.crossover.techtrial.java.se.objects.Currency;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/account-entries.xml")
@ActiveProfiles("test")
public class AccountDaoTest {

	 	@Autowired
	    private  IAccountDao accountDao;
	 	
	 	@After
		public void setup() {
	        accountDao.deleteAll();

		}
	 	
	 	@Test
	 	public void findById_IdDoesNotExist_ShouldReturnEmptyObjet(){
	 		
	 		Optional<Account> account=accountDao.findById("HSHSDSEOZEZ");
	 		assertFalse(account.isPresent());
	 	}
	 	
	 	@Test
	 	public void findById_idExist_ShouldReturnValue(){
	 		
	 		Optional<Account> account=accountDao.findById("KoXNPQA7Q");
	 		assertTrue(account.isPresent());
	 		assertThat(account.get()).isNotNull();
	 		assertThat(account.get().getId()).isEqualTo("KoXNPQA7Q");
	 		assertThat(account.get().getBalance()).isEqualTo(1000);
	 		assertThat(account.get().getCurrency()).isEqualTo(Currency.USD);

	 	}
}
