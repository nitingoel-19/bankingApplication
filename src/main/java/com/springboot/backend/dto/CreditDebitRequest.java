package com.springboot.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditDebitRequest {
	
	private String accountNumber;
	
	private double amount;

}
