package com.example.blah.serviceImpl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.blah.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	JavaMailSender mailSender;

	@Value("${spring.mail.username}")
    private String senderEmail;
	
	// 인증번호 생성
	@Override
	public String createCode() {
		Random random = new Random();
        StringBuilder key = new StringBuilder();
        
        // 인증코드 6자리
        for(int i=0;i<6;i++) {
        	// 0~1까지 랜덤, 랜덤값으로 switch문 실행
        	int index = random.nextInt(2);
        	
        	switch(index) {
        		case 0 -> key.append((char) (random.nextInt(26) + 65)); //대문자
        		case 1 -> key.append(random.nextInt(10)); //숫자
        	}
        }
        return key.toString();
	}
	
	// 이메일 발송
	@Override
	public String createMailsend(String sendEmail) throws MessagingException {
		
		String authCode = createCode(); // 랜덤 인증번호 생성

	        String body = "";
	        body += "<div style='margin:100px;'>";
	        body += "<h3>이메일을 인증하려면 다음 코드를 입력하세요.</h3>";
	        body += "<br>";
	        body += "<h1>" + authCode + "</h1>";
	        body += "<br>";
	        body += "<h3>이 메시지는 Blah Blah에서 보냈습니다.</h3>";
	        body += "<br>";
	        body += "<h3>감사합니다.</h3>";
	        body += "</div>";
	        
	        System.out.print("보내는사람 이메일=="+ senderEmail);
	        MimeMessage message = mailSender.createMimeMessage();
	        
        	message.setFrom(senderEmail); //보내는 이메일주소명
        	message.setRecipients(MimeMessage.RecipientType.TO, sendEmail); // 보내는 대상
        	message.setSubject("Blah Blah 회원가입 이메일 인증"); // 제목
        	message.setText(body, "UTF-8", "html"); // 내용
        	mailSender.send(message); // 메일 발송
//        	System.out.print("메세지=="+ message);
//        	System.out.print("인증번호==="+ authCode);
            return authCode; // 인증코드 일치 여부를 비교하기 위해 인증코드 반환
	       
	        
//	        try {
//	        	message.setFrom(senderEmail); //보내는 이메일주소명
//	        	message.setRecipients(MimeMessage.RecipientType.TO, sendEmail); // 보내는 대상
//	        	message.setSubject("Blah Blah 회원가입 이메일 인증"); // 제목
//	        	message.setText(body, "UTF-8", "html"); // 내용
//	        	mailSender.send(message); // 메일 발송
//	        	System.out.print("메세지=="+ message);
//	        	System.out.print("인증번호==="+ authCode);
//	            return authCode; // 인증코드 일치 여부를 비교하기 위해 인증코드 반환
//	        } catch (MailException e) {
//	        	String result = "fail";
//	        	return result;
//	        }
//	        
	}
	
//	@Override
//	public boolean sendSimpleMessage(String sendEmail) throws MessagingException {
//
//        MimeMessage message = createMail(sendEmail, authCode); // 메일 생성
//        try {
//        	mailSender.send(message); // 메일 발송
//            return true;
//        } catch (MailException e) {
//            return false;
//        }
//	}
}
