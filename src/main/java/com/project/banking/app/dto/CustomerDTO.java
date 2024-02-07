package com.project.banking.app.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerDTO {

	private String fullName;
	private String idCard;
	private String accountName;
	private double initialAmount;
	
}
