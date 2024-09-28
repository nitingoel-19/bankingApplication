package com.springboot.backend.utils;

import java.time.Year;

public class AccountUtils {
	
	public static String generateAccountNumber() {
		/**
		 * 
		 * 2023 + randomSixDigits number
		 * 
		 */
		Year currentYear = Year.now();
		
		int min = 100000;
		int max = 999999;
		
		int randNumber = (int) Math.floor(Math.random() * (max-min+1) + min);
		
		// concatenate year + randNumber(random Number);
		
		String year = String.valueOf(currentYear);
		String randomNumber = String.valueOf(randNumber);
		
		StringBuilder accountNum = new StringBuilder();
		accountNum.append(year);
		accountNum.append(randomNumber);
		
		return accountNum.toString();
	}
	
	public static final String Account_Exists_Code = "001";
	public static final String Account_Exists_Message = "This User already has an account";
	
	public static final String Account_Not_Exist_Code = "404";
	public static final String Account_Not_Exist_Message = "This User does not exists";
	
	
	public static final String Account_Credited_And_Debited_Code = "104";
	public static final String Account_Credited_And_Debited_Message = "Amount debited successfully"+" and "+ "Amount credited successfully";
	
	
	public static final String Destination_Account_Not_Exist_Code = "404";
	public static final String Destination_Account_Not_Exist_Message = "This User does not exists";
	
	public static final String Account_Found_Code = "004";
	public static final String Account_Found_Message = "User Account Found";
	
	public static final String Account_Credited_Code = "005";
	public static final String Account_Credited_Message = "Amount credited successfully";
	
	public static final String Account_Debit_Code = "006";
	public static final String Account_Debit_Message = "Amount debited successfully";
	
	public static final String LessAmount_Debit_Code = "909";
	public static final String LessAmount_Debit_Message = "Not sufficient balance";
	
	public static final String Account_Creation_Code = "002";
	public static final String Account_Creation_Message = "Account Created Successfully :)";
	
	
	
}
