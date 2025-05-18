package com.example.blah.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blah.service.EmailService;
import com.example.blah.service.JoinService;
import com.example.blah.service.RedisService;

import jakarta.mail.MessagingException;


@RestController
@RequestMapping("join/*")
public class JoinController {
	
	@Autowired
	JoinService service;
	
	@Autowired
	PasswordEncoder pwdEncoder;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	RedisService redisService;
	
	// 이메일 인증번호 전송
	@PostMapping("emailsend")
	public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> request) throws MessagingException {
	    String sendEmail = request.get("email");
	    System.out.println("수신자 이메일주소 : " + sendEmail);
	    String authCode = emailService.createMailsend(sendEmail);
	    System.out.println("이메일 전송 완료!!");
	    redisService.saveVerificationCode(sendEmail, authCode); // 인증코드 redis에 저장
	    return ResponseEntity.ok("이메일 전송 완료");
	}
	
	// 인증번호 검증
	@PostMapping("authCodeVerify")
	public String verifyEmail(@RequestBody Map<String, String> request) {
		String sendemail = request.get("email");
		String code = request.get("authcode");
		System.out.println("수신자 이메일주소 : " + sendemail);
		System.out.println("인증번호 : " + code);
		
		String result ="";
		if (redisService.verifyEmail(sendemail,code)) {
			result = "success";
        } else {
            result ="false";
        }
		return result;
		
	}
	
	// 아이디 중복확인
	@PostMapping("idCheck")
	public int idCheck(@RequestBody Map<String, String> request) {
		String userid = request.get("userId");
		System.out.println("이용자 아이디 : " + userid);
		int checkId = service.idCheck(userid);
		System.out.println("사용가능여부 : " + checkId);
		return checkId;
		
	}
	
	@PostMapping("userJoin")
	public Map<String, Object> checkEmail(@RequestParam(name="u_email", defaultValue="") String u_mail) {
		Map<String, Object> map = new HashMap<>();
		
		return map;
	}
	
//	public Map<String, Object> login(@RequestParam (name="u_id") String u_id, @RequestParam (name="u_password") String u_password) {
//		String passwd = dao.checkPw(u_id);
//		
//		Map<String, Object> map1 = dao.login(u_id, passwd);
//		Map<String, Object> map = new HashMap<>();
//		
//		String message = "";
//		
//		if(pwdEncoder.matches(u_password, passwd)){ // 로그인 성공
//			map.put("g_email", g_email);
//			map.put("g_name", map1.get("g_name"));
//			map.put("g_level", map1.get("g_level"));
//			map.put("g_idx", map1.get("g_idx"));
//			map.put("g_phone", map1.get("g_phone"));
//			map.put("g_profile", map1.get("g_profile"));
//			map.put("g_photo", map1.get("g_photo"));
//			map.put("message", "success");
//		} else {
//			message = "error";
//		}
//		return map;
//	}
	
	
}
