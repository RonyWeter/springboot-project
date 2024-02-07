package com.project.banking.app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.banking.app.dto.AccountDTO;
import com.project.banking.app.dto.AccountInfoDTO;
import com.project.banking.app.dto.AllTransactionsDTO;
import com.project.banking.app.dto.CustomerDTO;
import com.project.banking.app.dto.TransactionDTO;
import com.project.banking.app.model.Accounts;
import com.project.banking.app.model.Customers;
import com.project.banking.app.model.Transactions;
import com.project.banking.app.repository.AccountRepository;
import com.project.banking.app.repository.CustomerRepository;
import com.project.banking.app.repository.TransactionRepository;
import com.project.banking.backend.Response;
import com.project.banking.utils.Constants;

@Service
public class CustomerServiceImpl implements CustomerService {

	private CustomerRepository customerRepository;
	private AccountRepository accountRepository;
	private TransactionRepository transactionRepository;
	
	@Autowired
	public CustomerServiceImpl(CustomerRepository customerRepository, AccountRepository accountRepository,
			TransactionRepository transactionRepository) {
		super();
		this.customerRepository = customerRepository;
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	public Response getCustomersByFullNamePrefix(String prefix) {
		List<Customers> customersList = new ArrayList<Customers>();
		Response response = null;
		customersList= customerRepository.getAllCustomers(prefix);

		if (!customersList.isEmpty() && customersList.size() > 0)
			response = Response.builder()
								.success(true)
								.statusCode(Constants.STATUS_CODE_SUCCESS)
								.message(Constants.SUCCESS_MESSAGE)
								.data(customersList)
								.build();

		else
			response = Response.builder()
								.success(true)
								.statusCode(Constants.STATUS_CODE_EMPTY)
								.message(Constants.NO_DATA_FOUND_MESSAGE)
								.data(customersList)
								.build();

		return response;
	
	}

	@Override
	public Response deleteCustomer(int customerId) {
		Customers customer = customerRepository.findByCustomerId(customerId);
		List<Accounts> accountList = accountRepository.findByCustomerId(customerId);
		List<Transactions> transactionList = transactionRepository.findByCustomerId(customerId);
		Response response;
		
		if(customer != null && !accountList.isEmpty() && accountList.size() > 0) {
				accountRepository.deleteAccountByCustomerId(customerId);
				customerRepository.deleteById(customerId);
				if(!transactionList.isEmpty() && transactionList.size()>0) {
					transactionRepository.deleteTransactionsByCustomerId(customerId);
				}
				
			response = Response.builder()
								.success(true)
								.statusCode(Constants.STATUS_CODE_SUCCESS)
								.message(Constants.SUCCESSFULLY_DELETED)
								.build();
		}else {
			response = Response.builder()
					.success(false)
					.statusCode(Constants.STATUS_CODE_EMPTY)
					.message(Constants.NO_DATA_FOUND_MESSAGE)
					.build();
		}
		
		return response;
	}

	@Override
	public Response createCustomer(CustomerDTO customerDTO) {
		Response response = null;
		String idCard = customerDTO.getIdCard();
		Customers newCustomer = new Customers();
		Customers customer = customerRepository.findByIdCard(idCard);
		
		if (customer == null) {
			newCustomer.setFullName(customerDTO.getFullName());
			newCustomer.setIdCard(idCard);
			customerRepository.save(newCustomer);
			
			// Check if the customer saved in the Customers table
			customer = customerRepository.findByIdCard(idCard);
			if (customer != null) {
				Accounts account = new Accounts();
				account.setAccountName(customerDTO.getAccountName());
				account.setBalance(customerDTO.getInitialAmount());
				account.setInitialAmount(customerDTO.getInitialAmount());
				account.setCustomerId(customer.getCustomerId());
				accountRepository.save(account);
				response = Response.builder()
									.success(true)
									.statusCode(Constants.STATUS_CODE_SUCCESS)
									.message(Constants.SUCCESSFULLY_SAVED)
									.build();
			} else {
				response = Response.builder()
						.success(true)
						.statusCode(Constants.STATUS_CODE_FAILED)
						.message(Constants.CUSTOMER_NOT_CREATED)
						.build();
			}
		} else {
			response = Response.builder()
					.success(true)
					.statusCode(Constants.STATUS_CODE_FAILED)
					.message(Constants.CUSTOMER_ALREADY_EXIST)
					.build();
		}

		return response;
	}
	
	public Response getCustomerInfo(int customerId) {
		Response response = null;
		Customers customerInfo = customerRepository.findByCustomerId(customerId);
		if(customerInfo != null)
			response = Response.builder()
								.success(true)
								.statusCode(Constants.STATUS_CODE_SUCCESS)
								.message(Constants.SUCCESS_MESSAGE)
								.data(customerInfo)
								.build();
		else 
			response = Response.builder()
					.success(true)
					.statusCode(Constants.STATUS_CODE_EMPTY)
					.message(Constants.BAD_REQUEST)
					.data(customerInfo)
					.build();
		
		return response;
	}

	@Override
	public Response deleteAccount(AccountDTO accountDTO) {
		Response response = null;
		Accounts account = accountRepository.getAccountByAccIdAndCustId(accountDTO.getCustomerId(), accountDTO.getAccountId());
		if(account == null) {
			response = Response.builder()
					.success(false)
					.statusCode(Constants.STATUS_CODE_FAILED)
					.message(Constants.BAD_REQUEST)
					.build();
		}
		else {
			List<Accounts> accounts = accountRepository.findByCustomerId(accountDTO.getCustomerId());
			if(!accounts.isEmpty()) {
				if(accounts.size() > 1) {
					accountRepository.deleteAccountByAccountId(accountDTO.getAccountId());
					response = Response.builder()
							.success(true)
							.statusCode(Constants.STATUS_CODE_SUCCESS)
							.message(Constants.SUCCESSFULLY_DELETED)
							.build();
				}
				else {
					response = Response.builder()
							.success(false)
							.statusCode(Constants.STATUS_CODE_FAILED)
							.message(Constants.BAD_REQUEST)
							.build();
				}
				
			}
			else{
				response = Response.builder()
						.success(false)
						.statusCode(Constants.STATUS_CODE_EMPTY)
						.message(Constants.NO_DATA_FOUND_MESSAGE)
						.build();
			}
		}
	
		return response;
		
		
	}
	
	private Response saveTransactions(TransactionDTO transactionDTO,Double balance) {
		Response response = null;
		Transactions transaction = new Transactions();
		transaction.setAccountId(transactionDTO.getAccountId());
		transaction.setCustomerId(transactionDTO.getCustomerId());
		transaction.setNotes(transactionDTO.getNotes());
		transaction.setTranxAmount(transactionDTO.getTranxAmount());
		transaction.setTranxDate(transactionDTO.getTranxDate());
		transaction.setTranxType(String.valueOf(transactionDTO.getTranxType()));
		transaction.setToAccountId(transactionDTO.getToAccountId());
		transactionRepository.save(transaction);
		
		//Update Balance
		if(transactionDTO.getTranxType().equals("d")) {
			Double finalBalance = balance + transactionDTO.getTranxAmount();
			accountRepository.updateBalanceFromCustomerByAccountId(finalBalance, transactionDTO.getAccountId());
		}
		else if(transactionDTO.getTranxType().equals("w")){
			Double finalBalance = balance - transactionDTO.getTranxAmount();
			accountRepository.updateBalanceFromCustomerByAccountId(finalBalance, transactionDTO.getAccountId());
		}
		else {
			Double finalBalance = balance - transactionDTO.getTranxAmount();
			accountRepository.updateBalanceFromCustomerByAccountId(finalBalance, transactionDTO.getAccountId());
			
			accountRepository.updateBalanceToCustomerByAccountId(transactionDTO.getToAccountId(),transactionDTO.getTranxAmount());
			
		}
		
		response = Response.builder()
				.success(true)
				.statusCode(Constants.STATUS_CODE_SUCCESS)
				.message(Constants.SUCCESSFULLY_SAVED)
				.build();
		return response;
	}

	@Override
	public Response createTransaction(TransactionDTO transactionDTO) {
		Response response = null;
		Accounts toAccount = accountRepository.findByAccountId(transactionDTO.getToAccountId());
		Double balance = accountRepository.getBalanceOfAccountByAccId(transactionDTO.getAccountId());
		Accounts account = accountRepository.getAccountByAccIdAndCustId(transactionDTO.getCustomerId(), transactionDTO.getAccountId());
        if(account != null) {
        	if(transactionDTO.getToAccountId().equals(0)) {
        		if(transactionDTO.getTranxType().equals("w")) {
        			if(balance > transactionDTO.getTranxAmount()) {
        				response = saveTransactions(transactionDTO,balance);
        			}
        			else {
        				response = Response.builder()
        						.success(true)
        						.statusCode(Constants.STATUS_CODE_FAILED)
        						.message(Constants.SMALL_BALANCE)
        						.build();
        			}
        		}
        		else {
        			response = saveTransactions(transactionDTO,balance);
        		}
        	}
        	else {
        		if(toAccount != null) {
        			if(balance > transactionDTO.getTranxAmount()) {
        				response = saveTransactions(transactionDTO,balance);
        			}
        			else {
        				response = Response.builder()
        						.success(true)
        						.statusCode(Constants.STATUS_CODE_FAILED)
        						.message(Constants.SMALL_BALANCE)
        						.build();
        			}
        		}
        		else {
        			response = Response.builder()
    						.success(true)
    						.statusCode(Constants.STATUS_CODE_FAILED)
    						.message(Constants.ACCOUNT_TO_TRANSFER)
    						.build();
        		}
        	}
        }
        else {
        	response = Response.builder()
					.success(false)
					.statusCode(Constants.STATUS_CODE_EMPTY)
					.message(Constants.NO_DATA_FOUND_MESSAGE)
					.build();
        }
		return response;
	}

	@Override
	public Response getTransactions(AllTransactionsDTO allTransactionsDTO) {
		Response response = null;
		int accountId = allTransactionsDTO.getAccountId();
		List<Transactions> allTransactions = new ArrayList<Transactions>();
		Accounts account = accountRepository.findByAccountId(accountId);
		if(account != null) {
				allTransactions = transactionRepository.getTransactionsByAccountId(accountId,allTransactionsDTO.getStartDate(),allTransactionsDTO.getEndDate());
				if(!allTransactions.isEmpty() && allTransactions.size() > 0) {
					response = Response.builder()
							.success(true)
							.statusCode(Constants.STATUS_CODE_SUCCESS)
							.message(Constants.SUCCESS_MESSAGE)
							.data(allTransactions)
							.build();
				}
				else {
					response = Response.builder()
							.success(true)
							.statusCode(Constants.STATUS_CODE_SUCCESS)
							.message(Constants.NO_DATA_FOUND_MESSAGE)
							.build();
				}
			}
			else {
				response = Response.builder()
						.success(false)
						.statusCode(Constants.STATUS_CODE_EMPTY)
						.message(Constants.NO_DATA_FOUND_MESSAGE)
						.build();
			}
		return response;
	}

	@Override
	public Response createAccount(AccountInfoDTO accountInfoDTO) {
		Response response = null;
		Accounts account = new Accounts();
		Customers cunstomer = customerRepository.findByCustomerId(accountInfoDTO.getCustomerId());
		if(cunstomer != null) {
			account.setAccountName(accountInfoDTO.getAccountName());
			account.setBalance(accountInfoDTO.getInitialAmount());
			account.setCustomerId(accountInfoDTO.getCustomerId());
			account.setInitialAmount(accountInfoDTO.getInitialAmount());
			accountRepository.save(account);
			
			response = Response.builder()
					.success(true)
					.statusCode(Constants.STATUS_CODE_SUCCESS)
					.message(Constants.SUCCESSFULLY_SAVED)
					.build();
		}
		else {
			response = Response.builder()
					.success(false)
					.statusCode(Constants.STATUS_CODE_EMPTY)
					.message(Constants.NO_DATA_FOUND_MESSAGE)
					.build();
		}
		return response;
	}
}














