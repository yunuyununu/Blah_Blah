package com.example.blah.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public interface EmailService {
	
	String createCode();
	
	String createMailsend(String sendEmail) throws MessagingException;
}
