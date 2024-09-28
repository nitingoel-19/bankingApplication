package com.springboot.backend.service;

import com.springboot.backend.dto.BankResponse;
import com.springboot.backend.dto.CreditDebitRequest;
import com.springboot.backend.dto.EnquiryRequest;
import com.springboot.backend.dto.TransferRequest;
import com.springboot.backend.dto.UserRequest;

public interface UserService {
	
	BankResponse createAccount(UserRequest userRequest);
	
	BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
	
	String nameEnquiry(EnquiryRequest enquiryRequest);
	
	BankResponse creditAccount(CreditDebitRequest creditDebitRequest);
	
	BankResponse debitAccount(CreditDebitRequest creditDebitRequest);
	
	BankResponse transfer(TransferRequest transferRequest);

}
