package com.springboot.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
	
    private String transactionType;
	
	private double amount;
	
	private String accountNumber;
	
	private String status;

}
