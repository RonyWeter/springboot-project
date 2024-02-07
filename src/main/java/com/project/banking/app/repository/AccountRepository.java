package com.project.banking.app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.banking.app.model.Accounts;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Integer>{

	List<Accounts> findByCustomerId(int customerId);
	Accounts findByAccountId(int accountId);
	
	@Query("DELETE FROM Accounts WHERE customerId = ?1")
	@Modifying
	@Transactional
	void deleteAccountByCustomerId(int customerId);
	
	@Query("DELETE FROM Accounts WHERE accountId = ?1")
	@Modifying
	@Transactional
	void deleteAccountByAccountId(int accountId);
	
	@Query("select a FROM Accounts a WHERE customerId = ?1 and accountId = ?2")
	@Transactional
	Accounts getAccountByAccIdAndCustId(int customerId,int accountId);
	
	@Query("select a.balance FROM Accounts a WHERE  accountId = ?1")
	@Transactional
	Double getBalanceOfAccountByAccId(int accountId);
	
	@Query("UPDATE Accounts SET balance = ?1  WHERE accountId = ?2")
	@Modifying
	@Transactional
	void updateBalanceFromCustomerByAccountId(double balacnce,int accountId);
	
	
	@Query(value ="update Accounts " + 
			"	set balance = (select balance from Accounts where account_id = ?1) + ?2 " + 
			"	where account_id = ?1",nativeQuery = true)
	@Modifying
	@Transactional
	void updateBalanceToCustomerByAccountId(int accountId,Double transAmount);
	
}
