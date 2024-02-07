package com.project.banking.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountInfoDTO {

	  private int customerId; 
      private String accountName;
      private double initialAmount;
}
