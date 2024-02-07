package com.project.banking.app.repository;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.banking.app.model.Transactions;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Integer>{

	List<Transactions> findByCustomerId(int customerId);
	List<Transactions> findByAccountId(int accountId);

	
	@Query("DELETE FROM Transactions WHERE customerId = ?1")
	@Modifying
	@Transactional
	void deleteTransactionsByCustomerId(int customerId);
	
	@Query(value="\r\n" + 
			"SELECT * \r\n" + 
			"FROM Transactions\r\n" + 
			"WHERE account_id = ?1 and tranx_date BETWEEN ?2 AND ?3",nativeQuery = true)
	List<Transactions> getTransactionsByAccountId(int accountId,Date startDate,Date endDate);
}
