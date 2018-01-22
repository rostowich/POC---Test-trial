package com.crossover.techtrial.java.se.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crossover.techtrial.java.se.domain.User;

/**
 * This class represent the repository interface for user entity 
 * @author
 *
 */
@Repository
public interface IUserDao extends JpaRepository<User, Long>{

	/**
	 * Find the user by his ID
	 * @param Long userId
	 * @return Optional<User>
	 */
	Optional<User> findByUserId(Long userId);
	
	/**
	 * Find the user by his email address
	 * @param email
	 * @return Optional<User>
	 */
	Optional<User> findByEmail(String email);
	
	
	/**
	 * Find a list of user by %email%
	 * @param email
	 * @return
	 */
	Page<User> findByEmailContaining(String email, Pageable pageRequest); 
	
}
