package com.springboot.backend.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.backend.entity.Transaction;

public interface TransactionRepositiory extends JpaRepository<Transaction, String>{

}
