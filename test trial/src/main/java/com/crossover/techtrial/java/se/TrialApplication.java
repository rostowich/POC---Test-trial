package com.crossover.techtrial.java.se;

import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.crossover.techtrial.java.se.dao.IUserDao;
import com.crossover.techtrial.java.se.domain.Account;
import com.crossover.techtrial.java.se.domain.User;
import com.crossover.techtrial.java.se.objects.Currency;
import com.crossover.techtrial.java.se.objects.Role;

@SpringBootApplication
public class TrialApplication {
	
	private static final Logger log = LoggerFactory.getLogger(TrialApplication.class);
	
	@Autowired
	Environment env;
	
	public static void main(String[] args) {
		SpringApplication.run(TrialApplication.class, args);
	}
	
	/**
	 * This is used to save the admin account while running the application
	 * @param userRepository
	 * @return
	 */
	
	@Bean
	@Profile("!test")
	public CommandLineRunner beforeRunning(IUserDao userRepository) {
		return (args) -> {
			// save the admin user
			log.info("Saving the admin user");
			Optional<User> optionalUser=userRepository.findByEmail("admin@admin.com");
			
			if(!optionalUser.isPresent()){
				if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
					User user=new User();
					user.setEmail("admin@admin.com");
					user.setPassword(new BCryptPasswordEncoder().encode("administrator"));
					user.setRole(Role.ADMIN);
					user.setAccount(new Account("adminaccount", 0L, Currency.USD));
					
					userRepository.save(user); 
			      }
				
			}
		};
	}
}
