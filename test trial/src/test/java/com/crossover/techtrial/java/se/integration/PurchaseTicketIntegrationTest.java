package com.crossover.techtrial.java.se.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.servlet.Filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.crossover.techtrial.java.se.dao.IAccountDao;
import com.crossover.techtrial.java.se.dao.ITicketDao;
import com.crossover.techtrial.java.se.dao.IUserDao;
import com.crossover.techtrial.java.se.domain.Account;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.objects.AirlineOffer;
import com.crossover.techtrial.java.se.objects.AirlineRoute;
import com.crossover.techtrial.java.se.objects.Balance;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.objects.PurchaseTicket;
import com.crossover.techtrial.java.se.objects.Role;
import com.crossover.techtrial.java.se.objects.UserLogin;
import com.crossover.techtrial.java.se.objects.UserRegistration;
import com.crossover.techtrial.java.se.services.IExternalSystemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * This is the class in charge of the integration test. We will need to launch the entire Spring 
 * context on a random port. We will be 2 tickets from London To Madrid and reset the API
 * @author 
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("prod")
public class PurchaseTicketIntegrationTest {

	/**
	 * Autowire the random port into the variable so that we can use it to create the url
	 */
	@LocalServerPort
	private int port;
	
	/**
	 * Use to add create user before purchasing the ticket
	 */
	
	@Autowired
	private IExternalSystemService externalSystemService;
	
	@Autowired
	private ITicketDao ticketDao;
	
	@Autowired
	private IUserDao userDao;
	
	@Autowired
	private IAccountDao accountDao;
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private Filter springSecurityFilterChain;
	
	/**
	 * The initial account provided by the API for the integration test
	 */
	private final String initialAccountNumber="account1";
	
	private User user;
	
	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
	
	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders
				.webAppContextSetup(context)
				.defaultRequest(get("/").with(SecurityMockMvcRequestPostProcessors.user("user").roles("ADMIN")))
				.addFilter(springSecurityFilterChain)
				.build();
		//We have first to create the corresponding user and his account to 
		//purchase the 2 tickets
		ticketDao.deleteAll();
		userDao.deleteAll();
		accountDao.deleteAll();
		Account account=new Account(this.initialAccountNumber, 1000L, Currency.USD);
		user=new User("crossover@crossover.com", new BCryptPasswordEncoder().encode("password"), Role.USER, account);
		user=userDao.save(user);
		
	}
	
	@After
	public void tearDown() throws Exception{
		//After the test we have to reset the API and delete users and tickets from the database
		externalSystemService.reset();
		ticketDao.deleteAll();
		userDao.deleteAll();
		accountDao.deleteAll();
	}
	
	/**
	 * This method is used to test the purchase ticket method. We will purchase 2 tickets
	 * @throws Exception 
	 */
	@Test
	public void byeTicket() throws Exception{
		
		//We can now test the purchase ticket request using this user
		AirlineOffer offer=new AirlineOffer(new Balance(100L, Currency.EUR), new AirlineRoute("London", "Madrid"));
		PurchaseTicket purchaseTicket=new PurchaseTicket(2, offer, user.getUserId());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post(this.createURLWithPort("/tickets/user/purchase")).with(csrf())
				.accept(MediaType.APPLICATION_JSON_UTF8)
				.content(objectMapper.writeValueAsBytes(purchaseTicket))
				.contentType(MediaType.APPLICATION_JSON);

		//Now we can perform the method
		mockMvc.perform(requestBuilder)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.amount", is(100)))
		.andExpect(jsonPath("$.currency", is("EUR")))
		.andExpect(jsonPath("$.departure", is("London")))
		.andExpect(jsonPath("$.destination", is("Madrid")))
		.andExpect(jsonPath("$.number", is(2)))
		.andExpect(jsonPath("$.user.email", is("crossover@crossover.com")))
		.andExpect(jsonPath("$.user.role", is(Role.USER.toString())))
		.andExpect(jsonPath("$.user.account.id", is(initialAccountNumber)))
		.andExpect(jsonPath("$.user.account.balance", is(780)))
		.andExpect(jsonPath("$.user.account.currency", is("USD")));
	}
}
