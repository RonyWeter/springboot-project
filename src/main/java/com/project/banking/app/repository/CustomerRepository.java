package com.project.banking.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.banking.app.model.Customers;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer> {


	Customers findByCustomerId(int customerId);
	
	Customers findByIdCard(String idCard);
	
	@Query(value = "select c.full_name, c.id_card, a.account_id, a.account_name, a.balance, a.initial_amount "
			+ " from customers c, accounts a where c.customer_id = a.customer_id and c.customer_id = ?1", nativeQuery = true)
	List<Object> getCustomerInfo(int customerId);
	
	@Query(value = "SELECT * FROM Customers WHERE full_name LIKE %?1%", nativeQuery = true)
	List<Customers> getAllCustomers(String fullName);
}
