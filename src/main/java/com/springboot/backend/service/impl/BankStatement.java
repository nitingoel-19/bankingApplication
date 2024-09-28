package com.springboot.backend.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.springboot.backend.dto.EmailDetails;
import com.springboot.backend.entity.Transaction;
import com.springboot.backend.entity.User;
import com.springboot.backend.repositiory.TransactionRepositiory;
import com.springboot.backend.repositiory.UserRepositiory;
import com.springboot.backend.service.EmailService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {
	
	private TransactionRepositiory transactionRepositiory;
	
	private UserRepositiory userRepositiory;
	
	private EmailService emailService;
	
	private static final String FILE = "/Users/nitingoel/Downloads/MyStatement.pdf";
	
	/*
	 * 
	 * retrieve list of transactions within a date range given an account number
	 * generate a PDF file of transaction
	 * send the file via email
	 * 
	 */
	
	public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException{
		
		User user = userRepositiory.findByAccountNumber(accountNumber);
		
		LocalDate start = LocalDate.parse(startDate,DateTimeFormatter.ISO_DATE);
		
		LocalDate end = LocalDate.parse(endDate,DateTimeFormatter.ISO_DATE);
		
		List<Transaction> transactionList = transactionRepositiory.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
				.filter(transaction -> transaction.getCreatedAt().equals(start)).filter(transaction -> transaction.getCreatedAt().equals(end)).toList();
		
			
		designStatement(transactionList,user,startDate,endDate);
			
		
		return transactionList;
		
	}

	private void designStatement(List<Transaction> transactionList, User user, String startDate,String endDate) throws FileNotFoundException, DocumentException {
		
		String customerName = user.getFirstName()+" "+user.getLastName();
		
		Rectangle statementSize = new Rectangle(PageSize.A4);
		Document document = new Document(statementSize);
		log.info("setting the size of document");
		OutputStream outputStream = new FileOutputStream(FILE);
		PdfWriter.getInstance(document, outputStream);
		document.open();
		
		PdfPTable bankInfoTable = new PdfPTable(1);
		PdfPCell bankName = new PdfPCell(new Phrase("HDFC Bank"));
		bankName.setBorder(0);
		bankName.setBackgroundColor(BaseColor.BLUE);
		bankName.setPadding(20f);
		
		PdfPCell bankAddress = new PdfPCell(new Phrase("XYZ Address, Uttar Pradesh"));
		bankAddress.setBorder(0);
		bankInfoTable.addCell(bankName);
		bankInfoTable.addCell(bankAddress);
		
		PdfPTable statementInfo = new PdfPTable(2);
		PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date : "+startDate));
		customerInfo.setBorder(0);
		PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
		statement.setBorder(0);
		PdfPCell stopDate = new PdfPCell(new Phrase("End Date : "+endDate));
		stopDate.setBorder(0);
		PdfPCell customer = new PdfPCell(new Phrase("Customer Name : "+customerName));
		customer.setBorder(0);
		PdfPCell space = new PdfPCell();
		space.setBorder(0);
		PdfPCell address = new PdfPCell(new Phrase("Customer Address : "+user.getAddress()));
		address.setBorder(0);
		
		
		PdfPTable transactionsTable = new PdfPTable(4);
		PdfPCell date = new PdfPCell(new Phrase("DATE"));
		date.setBackgroundColor(BaseColor.BLUE);
		date.setBorder(0);
		PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
		transactionType.setBackgroundColor(BaseColor.BLUE);
		transactionType.setBorder(0);
		PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
		transactionAmount.setBackgroundColor(BaseColor.BLUE);
		transactionAmount.setBorder(0);
		PdfPCell status = new PdfPCell(new Phrase("STATUS"));
		status.setBackgroundColor(BaseColor.BLUE);
		status.setBorder(0);
		
		transactionsTable.addCell(date);
		transactionsTable.addCell(transactionType);
		transactionsTable.addCell(transactionAmount);
		transactionsTable.addCell(status);
		
		transactionList.forEach(transaction -> {
			transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
			transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
			transactionsTable.addCell(new Phrase(transaction.getAmount()+""));
			transactionsTable.addCell(new Phrase(transaction.getStatus()));
		});
		
		statementInfo.addCell(customerInfo);
		statementInfo.addCell(statement);
		statementInfo.addCell(endDate);
		statementInfo.addCell(customer);
		statementInfo.addCell(space);
		statementInfo.addCell(address);
		
		document.add(bankInfoTable);
		document.add(statementInfo);
		document.add(transactionsTable);
		
		document.close();
		
		EmailDetails emailDetails = EmailDetails.builder()
				.recepient(user.getEmail())
				.subject("STATEMENT OF ACCOUNT")
				.messageBody("Kindly find your requested account statement")
				.attachment(FILE)
				.build();
		emailService.sendEmailWithAttachment(emailDetails);
		
	}
	
}
