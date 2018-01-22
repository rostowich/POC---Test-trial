package com.crossover.techtrial.java.se.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crossover.techtrial.java.se.domain.Account;

/**
 * This class represent the repository interface for Account entity 
 * @author
 *
 */
@Repository
public interface IAccountDao extends JpaRepository<Account, String>{
	
	/**
	 * Find an account by its id
	 * @param id
	 * @return
	 */
	Optional<Account> findById(String id);
}
