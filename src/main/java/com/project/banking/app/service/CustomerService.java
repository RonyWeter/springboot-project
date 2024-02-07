package com.project.banking.app.service;

import com.project.banking.app.dto.AccountDTO;
import com.project.banking.app.dto.AccountInfoDTO;
import com.project.banking.app.dto.AllTransactionsDTO;
import com.project.banking.app.dto.CustomerDTO;
import com.project.banking.app.dto.TransactionDTO;
import com.project.banking.backend.Response;

public interface CustomerService {

	Response getCustomersByFullNamePrefix(String prefix);

	Response deleteCustomer(int customerId);
	
	Response createCustomer(CustomerDTO customerDTO);

	Response getCustomerInfo(int customerId);

	Response deleteAccount(AccountDTO accountDTO);

	Response createTransaction(TransactionDTO transactionDTO);

	Response getTransactions(AllTransactionsDTO allTransactionsDTO);

	Response createAccount(AccountInfoDTO accountInfoDTO);
}
