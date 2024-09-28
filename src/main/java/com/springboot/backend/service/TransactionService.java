package com.springboot.backend.service;

import com.springboot.backend.dto.TransactionDto;

public interface TransactionService {
	
	void saveTransaction(TransactionDto transactionDto);

}
