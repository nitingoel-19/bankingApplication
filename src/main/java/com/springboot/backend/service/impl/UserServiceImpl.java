package com.springboot.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.backend.dto.AccountInfo;
import com.springboot.backend.dto.BankResponse;
import com.springboot.backend.dto.CreditDebitRequest;
import com.springboot.backend.dto.EmailDetails;
import com.springboot.backend.dto.EnquiryRequest;
import com.springboot.backend.dto.TransactionDto;
import com.springboot.backend.dto.TransferRequest;
import com.springboot.backend.dto.UserRequest;
import com.springboot.backend.entity.User;
import com.springboot.backend.repositiory.UserRepositiory;
import com.springboot.backend.service.EmailService;
import com.springboot.backend.service.TransactionService;
import com.springboot.backend.service.UserService;
import com.springboot.backend.utils.AccountUtils;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	UserRepositiory userRepositiory;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	TransactionService transactionService;

	@Override
	public BankResponse createAccount(UserRequest userRequest) {
		/**
		 * Creating new User and saving in db
		 * 
		 * check if user already has an account
		 * 
		 */
		if(userRepositiory.existsByEmail(userRequest.getEmail())) {
			
			return BankResponse.builder()
					.responseCode(AccountUtils.Account_Exists_Code)
					.responseMessage(AccountUtils.Account_Exists_Message)
					.accountInfo(null)
					.build();
		}
		User newUser = User.builder()
				.firstName(userRequest.getFirstName())
				.lastName(userRequest.getLastName())
				.gender(userRequest.getGender())
				.maritalStatus(userRequest.getMaritalStatus())
				.address(userRequest.getAddress())
				.stateOfOrigin(userRequest.getStateOfOrigin())
				.email(userRequest.getEmail())
				.accountNumber(AccountUtils.generateAccountNumber())
				.phoneNumber(userRequest.getPhoneNumber())
				.accountBalance(0.0)
				.build();
		User savedUser = userRepositiory.save(newUser);
		
		// send email alert
		EmailDetails emailDetails = EmailDetails.builder()
				.recepient(savedUser.getEmail())
				.subject("ACCOUNT CREATION")
				.messageBody("Congratualtion! Your account has been created."+"\n"+"Account Holder Name : "+" "+savedUser.getFirstName()+" "+savedUser.getLastName())
				.build();
		emailService.sendEmailAlert(emailDetails);
		
		return BankResponse.builder()
				.responseCode(AccountUtils.Account_Creation_Code)
				.responseMessage(AccountUtils.Account_Creation_Message)
				.accountInfo(AccountInfo.builder()
						.accountName(savedUser.getFirstName() +" "+ savedUser.getLastName())
						.accountNumber(savedUser.getAccountNumber())
						.accountBalance(savedUser.getAccountBalance())
						.build())
				.build();
	}

	@Override
	public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
		
		// provided account number is present or not in db
		if(userRepositiory.existsByAccountNumber(enquiryRequest.getAccountNumber())) {
			
			User foundUser = userRepositiory.findByAccountNumber(enquiryRequest.getAccountNumber());
			
			return BankResponse.builder()
					.responseCode(AccountUtils.Account_Found_Code)
					.responseMessage(AccountUtils.Account_Found_Message)
					.accountInfo(AccountInfo.builder()
							.accountName(foundUser.getFirstName() +" "+ foundUser.getLastName())
							.accountNumber(foundUser.getAccountNumber())
							.accountBalance(foundUser.getAccountBalance())
							.build())
					.build();
		}
		else {
			return BankResponse.builder()
					.responseCode(AccountUtils.Account_Not_Exist_Code)
					.responseMessage(AccountUtils.Account_Not_Exist_Message)
					.accountInfo(null)
					.build();
		}
	}

	@Override
	public String nameEnquiry(EnquiryRequest enquiryRequest) {
		
		
		if(userRepositiory.existsByAccountNumber(enquiryRequest.getAccountNumber())) {
					
			User foundUser = userRepositiory.findByAccountNumber(enquiryRequest.getAccountNumber());
			
			return foundUser.getFirstName() +" "+ foundUser.getLastName();
		}
		else {
			return AccountUtils.Account_Not_Exist_Message;
		}
	}

	@Override
	public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
		
		// checking is account exists
		if(userRepositiory.existsByAccountNumber(creditDebitRequest.getAccountNumber())) {
					
			User userToCredit = userRepositiory.findByAccountNumber(creditDebitRequest.getAccountNumber());
			
			userToCredit.setAccountBalance(userToCredit.getAccountBalance() + creditDebitRequest.getAmount());
			
			userRepositiory.save(userToCredit);
			
			TransactionDto transactionDto = TransactionDto.builder()
					.accountNumber(userToCredit.getAccountNumber())
					.transactionType("CREDIT")
					.amount(creditDebitRequest.getAmount())
					.build();
			
			transactionService.saveTransaction(transactionDto);
			
			return BankResponse.builder()
					.responseCode(AccountUtils.Account_Credited_Code)
					.responseMessage(AccountUtils.Account_Credited_Message)
					.accountInfo(AccountInfo.builder()
							.accountName(userToCredit.getFirstName() +" "+ userToCredit.getLastName())
							.accountNumber(userToCredit.getAccountNumber())
							.accountBalance(userToCredit.getAccountBalance())
							.build())
					.build();
		}
		else {
			return BankResponse.builder()
					.responseCode(AccountUtils.Account_Not_Exist_Code)
					.responseMessage(AccountUtils.Account_Not_Exist_Message)
					.accountInfo(null)
					.build();
		}
	}

	@Override
	public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
		
		if(userRepositiory.existsByAccountNumber(creditDebitRequest.getAccountNumber())) {
			
			User userToDebit = userRepositiory.findByAccountNumber(creditDebitRequest.getAccountNumber());
			
			if(userToDebit.getAccountBalance() > 0 && creditDebitRequest.getAmount() > 0 && userToDebit.getAccountBalance() >= creditDebitRequest.getAmount()) {
			
				userToDebit.setAccountBalance(userToDebit.getAccountBalance() - creditDebitRequest.getAmount());
				
				userRepositiory.save(userToDebit);
				
				TransactionDto transactionDto = TransactionDto.builder()
						.accountNumber(userToDebit.getAccountNumber())
						.transactionType("DEBIT")
						.amount(creditDebitRequest.getAmount())
						.build();
				
				transactionService.saveTransaction(transactionDto);
				
				return BankResponse.builder()
						.responseCode(AccountUtils.Account_Debit_Code)
						.responseMessage(AccountUtils.Account_Debit_Message)
						.accountInfo(AccountInfo.builder()
								.accountName(userToDebit.getFirstName() +" "+ userToDebit.getLastName())
								.accountNumber(userToDebit.getAccountNumber())
								.accountBalance(userToDebit.getAccountBalance())
								.build())
						.build();
			}
			else {
				return BankResponse.builder()
						.responseCode(AccountUtils.LessAmount_Debit_Code)
						.responseMessage(AccountUtils.LessAmount_Debit_Message)
						.accountInfo(null)
						.build();
			}
	    }
		else {
			return BankResponse.builder()
					.responseCode(AccountUtils.Account_Not_Exist_Code)
					.responseMessage(AccountUtils.Account_Not_Exist_Message)
					.accountInfo(null)
					.build();
		}
	}

	@Override
	public BankResponse transfer(TransferRequest transferRequest) {
		
		/*  get the account to debit
		 * 
		    check the amount i'm debitting is not more than the current balance
		    
			debit the amount 
			
			get the account to credit
			
			credit the account
		*/
		
		boolean isDesinationAccountExists = userRepositiory.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
		
		if(!isDesinationAccountExists) {
			return BankResponse.builder()
					.responseCode(AccountUtils.Destination_Account_Not_Exist_Code)
					.responseMessage(AccountUtils.Destination_Account_Not_Exist_Message)
					.accountInfo(null)
					.build();
		}
		User sourceAccountUser = userRepositiory.findByAccountNumber(transferRequest.getSourceAccountNumber());
		
		String sourceName = sourceAccountUser.getFirstName()+" "+sourceAccountUser.getLastName();
		
		User destinationUser = userRepositiory.findByAccountNumber(transferRequest.getDestinationAccountNumber());
		
		if(sourceAccountUser.getAccountBalance() > 0 && transferRequest.getAmount() > 0 && sourceAccountUser.getAccountBalance() >= transferRequest.getAmount()) {
			
			sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance() - transferRequest.getAmount());
            userRepositiory.save(sourceAccountUser);
            
            EmailDetails debitAlert = EmailDetails.builder()
            		.subject("Debit Alert")
            		.recepient(sourceAccountUser.getEmail())
            		.messageBody("The sum of "+transferRequest.getAmount()+" has been deducted from your account!"+"\n" + "Your current balance is "+sourceAccountUser.getAccountBalance())
            		.build();
            emailService.sendEmailAlert(debitAlert);
            
            destinationUser.setAccountBalance(destinationUser.getAccountBalance() + transferRequest.getAmount());
            userRepositiory.save(destinationUser);
            
            EmailDetails creditAlert = EmailDetails.builder()
            		.subject("Credit Alert")
            		.recepient(destinationUser.getEmail())
            		.messageBody("The sum of "+transferRequest.getAmount()+" has been credit to your account by"+" "+sourceName +" Location of sender"+" "+sourceAccountUser.getAddress()+" "+"\n" + "and your location"+" "+destinationUser.getAddress())
            		.build();
            emailService.sendEmailAlert(creditAlert);
            
            TransactionDto transactionDto1 = TransactionDto.builder()
					.accountNumber(sourceAccountUser.getAccountNumber())
					.transactionType("DEBIT")
					.amount(sourceAccountUser.getAccountBalance())
					.build();
			
			transactionService.saveTransaction(transactionDto1);
			
			TransactionDto transactionDto2 = TransactionDto.builder()
					.accountNumber(destinationUser.getAccountNumber())
					.transactionType("CREDIT")
					.amount(destinationUser.getAccountBalance())
					.build();
			
			transactionService.saveTransaction(transactionDto2);
			
			return BankResponse.builder()
					.responseCode(AccountUtils.Account_Credited_And_Debited_Code)
					.responseMessage(AccountUtils.Account_Credited_And_Debited_Message)
					.build();
		}
		else {
			return BankResponse.builder()
					.responseCode(AccountUtils.LessAmount_Debit_Code)
					.responseMessage(AccountUtils.LessAmount_Debit_Message)
					.accountInfo(null)
					.build();
		}
	}
}
