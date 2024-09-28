package com.springboot.backend.repositiory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.backend.entity.User;

public interface UserRepositiory extends JpaRepository<User, Long>{
	
	boolean existsByEmail(String email);
	
	boolean existsByAccountNumber(String accountNumber);
	
	User findByAccountNumber(String accountNumber);

}
