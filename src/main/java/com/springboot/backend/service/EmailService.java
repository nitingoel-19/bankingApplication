package com.springboot.backend.service;

import com.springboot.backend.dto.EmailDetails;

public interface EmailService {

	void sendEmailAlert(EmailDetails emailDetails);
	
	void sendEmailWithAttachment(EmailDetails emailDetails);
}
