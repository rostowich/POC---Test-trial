package com.crossover.techtrial.java.se.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.domain.User;

/**
 * This class represent the repository interface for ticket entity 
 * @author
 *
 */
@Repository
public interface ITicketDao extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

	/**
	 * Find the ticket for a specific user
	 * @param user
	 * @param page
	 * @return
	 */
	Page<Ticket> findByUser(User user, Pageable page);
		
}
