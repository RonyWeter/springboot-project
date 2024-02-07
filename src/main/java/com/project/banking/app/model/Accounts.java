package com.project.banking.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "accounts")
public class Accounts {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private int accountId;
	
	@Column(name = "customer_id")
	private int customerId;
	
	@Column(name = "account_name")
	private String accountName;
	
	private Double balance;
	
	@Column(name = "initial_amount")
	private double initialAmount;
	
}
