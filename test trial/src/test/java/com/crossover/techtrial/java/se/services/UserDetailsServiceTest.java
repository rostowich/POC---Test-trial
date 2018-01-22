package com.crossover.techtrial.java.se.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.techtrial.java.se.dao.IUserDao;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.objects.Role;

/**
 * This class is used to unit test UserDetailsService class
 * @author
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UserDetailsServiceTest {

	//We will mock the abstract method and any method calling web services of external
	//system for unit tests
	
	@InjectMocks
	private UserDetailsServiceImpl userDetailsService;
	
	@Mock
	private  IUserDao userDao;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void loadUserByUsername_WillReturn_UsernameNotFoundException(){
		
		//We mock the findByEmail method of UserDao bean
		when(userDao.findByEmail("test@test.com")).thenReturn(Optional.empty());
		
		//Now we can test the method
		try{
		userDetailsService.loadUserByUsername("test@test.com");
		}
		catch(Exception e){
			assertThat(e)
			.isInstanceOf(UsernameNotFoundException.class)
            .hasMessage("User not found");
		}
	}
	
	@Test
	public void loadUserByUsername_WillReturn_UserDetails(){
		
		//We mock the findByEmail method of UserDao bean
		User user =new User("test@test.com", "password", Role.USER);
		when(userDao.findByEmail("test@test.com")).thenReturn(Optional.of(user));
		
		//Now we can test the method
		UserDetails userDetails= userDetailsService.loadUserByUsername("test@test.com");
		assertThat(userDetails).isInstanceOf(UserDetails.class);
		assertThat(userDetails.getUsername()).isEqualTo("test@test.com");
		assertThat(userDetails.getAuthorities().size()).isEqualTo(1);
		Iterator<? extends GrantedAuthority> iteratorOfAuthorities=userDetails.getAuthorities().iterator();
	    assertThat(iteratorOfAuthorities.next().getAuthority()).isEqualTo("USER");
	}
}

