package com.example.blah.service;

import jakarta.mail.MessagingException;

public interface EmailService {
	
	String createCode();
	
	String createMailsend(String sendEmail) throws MessagingException;
}
