package com.springboot.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.backend.dto.BankResponse;
import com.springboot.backend.dto.CreditDebitRequest;
import com.springboot.backend.dto.EnquiryRequest;
import com.springboot.backend.dto.TransferRequest;
import com.springboot.backend.dto.UserRequest;
import com.springboot.backend.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping
	public BankResponse createAccount(@RequestBody UserRequest userRequest) {
		return userService.createAccount(userRequest);
	}
	
	@GetMapping("/balanceEnquiry")
	public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		return userService.balanceEnquiry(enquiryRequest);
	}
	
	@GetMapping("nameEnquiry")
	public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest) {
		return userService.nameEnquiry(enquiryRequest);
	}
	
	@PutMapping("credit")
	public BankResponse creditAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
		return userService.creditAccount(creditDebitRequest);
	}
	
	@PutMapping("debit")
	public BankResponse debitAccount(@RequestBody CreditDebitRequest creditDebitRequest) {
		return userService.debitAccount(creditDebitRequest);
	}
	
	@PutMapping("transfer")
	public BankResponse transfer(@RequestBody TransferRequest transferRequest) {
		return userService.transfer(transferRequest);
	}
	
}
