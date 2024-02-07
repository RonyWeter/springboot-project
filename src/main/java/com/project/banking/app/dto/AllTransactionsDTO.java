package com.project.banking.app.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AllTransactionsDTO {

	private int accountId;
	private Date startDate;
	private Date endDate;
}
