package com.project.banking.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "transactions")
public class Transactions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tranx_id")
	private int tranxId;
	
	@Column(name = "tranx_type",columnDefinition = "nvarchar(1)")
	private String tranxType;
	
	@Column(name = "customer_id")
	private int customerId;
	
	@Column(name = "account_id")
	private int accountId;
	
	@Column(name = "tranx_amount")
	private Double tranxAmount;
	
	@Column(name = "to_account_id")
	private Integer toAccountId;
	
	private String notes;
	
	@Column(name = "tranx_date")
	private Date tranxDate;
}
