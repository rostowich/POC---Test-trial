package com.crossover.techtrial.java.se.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crossover.techtrial.java.se.dao.IUserDao;
import com.crossover.techtrial.java.se.domain.User;

/**
 * This is the implementation for UserDetailsService
 * @author
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
	@Autowired
	private  IUserDao userDao;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		try {	
			
		Optional<User> user = userDao.findByEmail(email);
		if(user.isPresent()){
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			grantedAuthorities.add(new SimpleGrantedAuthority(user.get().getRole().name()));
			return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), grantedAuthorities);
		}
		else{
			throw new UsernameNotFoundException("User not found");
		}
		} catch (Exception e) {
			throw new UsernameNotFoundException("User not found");
		}
	 }
	
}