package com.springboot.backend.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;
import com.springboot.backend.entity.Transaction;
import com.springboot.backend.service.impl.BankStatement;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/bankStatement")
@AllArgsConstructor
public class BankStatementController {
	
	private BankStatement bankStatement;
	
	@GetMapping
	public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
			@RequestParam String startDate,
			@RequestParam String endDate) throws FileNotFoundException, DocumentException{
		
		return bankStatement.generateStatement(accountNumber, startDate, endDate);
		
	}

}
