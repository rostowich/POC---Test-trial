package com.crossover.techtrial.java.se;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.test.context.ActiveProfiles;

import com.crossover.techtrial.java.se.auth.RESTAuthenticationEntryPoint;
import com.crossover.techtrial.java.se.auth.RESTAuthenticationFailureHandler;
import com.crossover.techtrial.java.se.auth.RESTAuthenticationSuccessHandler;
import com.crossover.techtrial.java.se.objects.Role;


@EnableWebSecurity
@ActiveProfiles("prod")
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	/**
	 * We need a custom authenticationEntryPoint because default Spring-Security config will 
	 * redirect to login page
	 */
	@Autowired
    private RESTAuthenticationEntryPoint authenticationEntryPoint;
	
	/**
	 * We need a custom AuthenticationFailureHandler. It will return http status code 401
	 */
    @Autowired
    private RESTAuthenticationFailureHandler authenticationFailureHandler;
    
    /**
	 * We need a custom AuthentificationSuccessHandler. It will return http status 200 with 
	 * user info in json format.
	 */
    @Autowired
    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception{
		httpSecurity
		.authorizeRequests()
			.antMatchers("/","/authenticate","/users/register","/index.html/**","/login.html","/register.html","/home.html","/bootstrap-3.0.3-dist/**","/angular-1.6.2/**","/angularjs-datepicker/src/**","/css/**","/js/**","/img/**","/pdf/*").permitAll()
			.antMatchers("/admin.html","/order.html","/tickets", "/tickets/all","/tickets/user","/users","/users/all").hasAuthority(Role.ADMIN.name())
			.antMatchers("/user.html","/pdf","/mail","/offers","/tickets","/tickets/user","/users").hasAuthority(Role.USER.name())
			.anyRequest().authenticated()
		.and()
			.formLogin()
			.usernameParameter("email")
			.passwordParameter("password")
			.loginPage("/login")
		.and()
            .logout()
            .logoutUrl("/logout")
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
            .deleteCookies("JSESSIONID")
            .permitAll()
		.and()
			.csrf()
			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
		.and()
			.exceptionHandling().accessDeniedPage("/Access_Denied");
		httpSecurity.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		httpSecurity.formLogin().successHandler(authenticationSuccessHandler);
		httpSecurity.formLogin().failureHandler(authenticationFailureHandler);
	}
	
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(new BCryptPasswordEncoder());
		 
    }
	
	@Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
