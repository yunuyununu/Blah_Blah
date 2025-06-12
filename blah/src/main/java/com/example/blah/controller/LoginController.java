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

import com.example.blah.domain.LoginDTO;
import com.example.blah.service.LoginService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {
	
	@Autowired
	LoginService service;
	
	@Autowired
	PasswordEncoder pwdEncoder;
	
	// 회원 로그인
	@PostMapping("/userlogin")
	public ResponseEntity<?> userLogin(@RequestBody Map<String, String> request, HttpSession session) {
		
		String userId = request.get("userId");
		String userPw = request.get("userPw");
		System.out.println("사용자 아이디 : " + userId);
		System.out.println("사용자 비밀번호 : " + userPw);

	    LoginDTO login = service.userLogin(userId,userPw);
	    String withdraw = service.userWithdrawYN(userId);
	    
	    if (login != null && withdraw.equals("N")) { // 로그인 성공
	    	session.setAttribute("UserIdx", login.getU_idx());
	    	
	    	return ResponseEntity.ok(Map.of("result", "success", "userIdx", session.getAttribute("UserIdx")));
	    } else if(login != null && withdraw.equals("Y")) {
	    	return ResponseEntity.ok(Map.of("result","withdraw"));
		} else {
			return ResponseEntity.ok(Map.of("result","fail"));
	    }
	}
	
	// 세션체크
	@PostMapping("/checkSession")
	public ResponseEntity<?> checkSession(HttpSession session) {
	    Object loginUser = session.getAttribute("UserIdx");
	    
	    if (loginUser != null) {
	        return ResponseEntity.ok(Map.of("isLogin", true, "userIdx", loginUser));
	    } else {
	        return ResponseEntity.ok(Map.of("isLogin", false));
	    }
	} 
	
	// 회원 로그아웃
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
	    session.invalidate();
	    return ResponseEntity.ok("로그아웃 완료");
	}
	
	// 아이디 찾기
	@PostMapping("/searchId")
	public LoginDTO searchId(@RequestBody Map<String, String> request) {
		String userEmail = request.get("email");
		String userPhone = request.get("userTel");
		
		Map<String, String> map = new HashMap<>();
		map.put("u_email",userEmail);
		map.put("u_phone",userPhone.replaceAll("-", ""));

		LoginDTO result = service.searchId(map);
		
	    return result;
	}
	
	// 비밀번호 찾기
	@PostMapping("/searchPw")
	public LoginDTO searchPw(@RequestBody Map<String, String> request) {
		
		String userId = request.get("userId");
		String userEmail = request.get("email");
		
		Map<String, String> map = new HashMap<>();
		map.put("u_id",userId);
		map.put("u_email",userEmail);

		LoginDTO result = service.searchPw(map);
		
	    return result;
	}
	
	// 비밀번호 찾기 (재설정)
	@PostMapping("/pwReset")
	public String searchPw(@RequestParam(name="idx", defaultValue="") int idx,
			@RequestParam(name="userPw", defaultValue="") String userPw,
			@RequestParam(name="userId", defaultValue="") String userId,HttpSession session) {
		try {
			// 비밀번호 암호화
	        String encodedPw = pwdEncoder.encode(userPw);
	        
	        // dto에 담아 보내기
	        LoginDTO dto = new LoginDTO();
	        dto.setU_idx(idx);
	        dto.setU_id(userId);
	        dto.setU_password(encodedPw);
	        
		    String result = service.pwReset(dto);
		    System.out.println("결과값 result : " + result);
		    session.invalidate(); 
		    
		    return result;
		} catch (Exception e) {
			e.printStackTrace();
	        return "fail";
		}
	}
}
