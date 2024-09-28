package com.springboot.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springboot.backend.dto.TransactionDto;
import com.springboot.backend.entity.Transaction;
import com.springboot.backend.repositiory.TransactionRepositiory;
import com.springboot.backend.service.TransactionService;

@Component
public class TransactionServiceImpl implements TransactionService{

	@Autowired
	TransactionRepositiory transactionRepositiory;
	
	@Override
	public void saveTransaction(TransactionDto transactionDto) {
		
		Transaction transaction = Transaction.builder()
				.transactionType(transactionDto.getTransactionType())
				.accountNumber(transactionDto.getAccountNumber())
				.amount(transactionDto.getAmount())
				.status("Success")
				.build();
		transactionRepositiory.save(transaction);
		System.out.println("Transaction saved successfully");
	}

}
