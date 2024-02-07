package com.project.banking.app.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.banking.app.dto.AccountDTO;
import com.project.banking.app.dto.AccountInfoDTO;
import com.project.banking.app.dto.AllTransactionsDTO;
import com.project.banking.app.dto.CustomerDTO;
import com.project.banking.app.dto.TransactionDTO;
import com.project.banking.app.service.CustomerService;
import com.project.banking.backend.Response;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

	@Autowired
	public CustomerService customerService;

	@GetMapping("/getCustomersByFullNamePrefix")
	public Response getCustomersByFullNamePrefix(@RequestParam(value = "prefix", required = false) String prefix) {
		return customerService.getCustomersByFullNamePrefix(prefix);
	}
	
	@DeleteMapping("/deleteCustomer")
	public Response deleteCustomer(@RequestParam("customerId") int customerId) {
		return customerService.deleteCustomer(customerId);
	}
	
	@PostMapping("/createCustomer")
	public Response createCustomer(@RequestBody CustomerDTO customerDTO) {
		return customerService.createCustomer(customerDTO);
	}
	
	@GetMapping("/getCustomerInfo")
	public Response getCustomerInfo(@RequestParam int customerId) {
		return customerService.getCustomerInfo(customerId);
	}
	
	@PostMapping("/deleteAccount")
	public Response deleteAccount(@RequestBody AccountDTO accountDTO) {
		return customerService.deleteAccount(accountDTO);
	}
	
	@PostMapping("/createTransaction")
	public Response createTransaction(@RequestBody TransactionDTO transactionDTO) {
		return customerService.createTransaction(transactionDTO); 
	}
	
	@PostMapping("/getTransactions")
	public Response getTransactions(@RequestBody AllTransactionsDTO allTransactionsDTO) {
		return customerService.getTransactions(allTransactionsDTO);
	}
	
	@PostMapping("/createAccount")
	public Response createAccount(@RequestBody AccountInfoDTO accountInfoDTO) {
		return customerService.createAccount(accountInfoDTO); 
	}
	
}
