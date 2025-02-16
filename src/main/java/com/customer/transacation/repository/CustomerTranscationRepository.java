package com.customer.transacation.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.customer.transacation.domain.CustomerTransaction;
import org.springframework.data.repository.query.Param;

@SuppressWarnings("unused")
@Repository
public interface CustomerTranscationRepository extends JpaRepository<CustomerTransaction, Long> {

	@Query("SELECT t FROM CustomerTransaction t")
	Collection<CustomerTransaction> getTransactions();

	 @Query("SELECT ct.customerId FROM CustomerTransaction ct WHERE ct.customerId = :customerId")
	 Long findCustomerIdByCustomerId(@Param("customerId") Long customerId);
	}