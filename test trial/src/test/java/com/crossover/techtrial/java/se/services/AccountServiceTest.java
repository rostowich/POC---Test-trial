package com.crossover.techtrial.java.se.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.techtrial.java.se.dao.IAccountDao;
import com.crossover.techtrial.java.se.domain.Account;
import com.crossover.techtrial.java.se.objects.Currency;

/**
 * This class is used to unit test AccountService class
 * @author
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AccountServiceTest {
	
	//We will mock the abstract method and any method calling web services of external
	//system for unit tests
	
	@InjectMocks
	private AccountServiceImpl accountService;
	
	@Mock
	private IAccountDao accountDao;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testSaveAccount(){
		Account account=new Account("RE323DGD2", 0L, Currency.USD);
		when(accountDao.save(account)).thenReturn(account);
		
		Account result=accountService.save(account);
		assertThat(result.getId()).isEqualTo("RE323DGD2");
		assertThat(result.getBalance()).isEqualTo(0L);
		assertThat(result.getCurrency()).isEqualTo(Currency.USD);
	}
}
