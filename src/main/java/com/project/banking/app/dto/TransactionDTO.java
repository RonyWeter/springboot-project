package com.project.banking.app.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {

	private String tranxType;
	private int customerId;
	private int accountId;
	private Double tranxAmount;
	private Integer toAccountId;
	private String notes;
	private Date tranxDate;

}
