package com.crossover.techtrial.java.se.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.crossover.techtrial.java.se.domain.Ticket;

/**
 * This class is used to search tickets specifying the criteria
 *
 */
public class TicketSpecification implements Specification<Ticket>{

	private Ticket filter;
	
	public TicketSpecification(Ticket filter) {
		super();
		this.filter=filter;
	}
	 
	@Override
	public Predicate toPredicate(Root<Ticket> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {

		Predicate predicate=criteriaBuilder.conjunction();
		if(filter.getId()!=null){
			predicate.getExpressions().add(criteriaBuilder.equal(root.get("id"), filter.getId()));
		}
		if(filter.getDeparture()!=null){
			predicate.getExpressions().add(criteriaBuilder.like(root.get("departure"), "%"+filter.getDeparture()+"%")); 
		}
		if(filter.getDestination()!=null){
			predicate.getExpressions().add(criteriaBuilder.like(root.get("destination"), "%"+filter.getDestination()+"%"));
		}
		if(filter.getAmount()!=null){
			predicate.getExpressions().add(criteriaBuilder.equal(root.get("amount"), filter.getAmount()));
		}
		if(filter.getBeginDate()!=null){
			predicate.getExpressions().add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), filter.getBeginDate()));
		}
		if(filter.getEndDate()!=null){
			predicate.getExpressions().add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), filter.getEndDate()));
		}
		if(filter.getNumber()!=null){
			predicate.getExpressions().add(criteriaBuilder.equal(root.get("number"), filter.getNumber()));
		}
		if(filter.getUser()!=null){
			if(filter.getUser().getEmail()!=null){
				predicate.getExpressions().add(criteriaBuilder.like(root.join("user").get("email"), "%"+filter.getUser().getEmail()+"%"));
			}
		}
		return criteriaBuilder.and(predicate);
	}

}
