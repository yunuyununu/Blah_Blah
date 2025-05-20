package com.example.blah.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blah.domain.LoginDTO;
import com.example.blah.service.LoginService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	LoginService service;
	
	// 회원 로그인
	@PostMapping("/userlogin")
	public String userLogin(@RequestBody Map<String, String> request, HttpSession session) {
		String userId = request.get("userId");
		String userPw = request.get("userPw");
		System.out.println("사용자 아이디 : " + userId);
		System.out.println("사용자 비밀번호 : " + userPw);

	    LoginDTO login = service.userLogin(userId,userPw);

	    if (login != null) { // 로그인 성공
	    	session.setAttribute("userId", login.getU_id());
	    	return "success";
	    } else {
	    	return "fail";
	    }
	    
	}
	
	// 세션체크
	@GetMapping("/checkSession")
	public ResponseEntity<?> checkSession(HttpSession session) {
	    Object loginUser = session.getAttribute("userId");
	    if (loginUser != null) {
	        return ResponseEntity.ok(true);
	    } else {
	        return ResponseEntity.ok(false);
	    }
	} 
	
	// 회원 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
	    session.invalidate();
	    return ResponseEntity.ok("로그아웃 완료");
	}
	
}
