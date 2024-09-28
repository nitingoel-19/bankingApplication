package com.springboot.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
	
    private String firstName;
	
	private String lastName;
	
	private String gender;
	
	private String maritalStatus;
	
	private String address;
	
	private String stateOfOrigin;
	
	private String email;
	
	private String phoneNumber;

}
