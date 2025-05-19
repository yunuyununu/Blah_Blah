package com.example.blah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blah.domain.LoginDTO;
import com.example.blah.service.LoginService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("login/*")
public class LoginController {
	
	@Autowired
	LoginService service;
	
	@Autowired
	PasswordEncoder pwdEncoder;
	
	@PostMapping("userlogin")
	public String userLogin(@RequestParam(name="userId", defaultValue="") String userId,
	                     @RequestParam(name="userPw", defaultValue="") String userPw,HttpSession session) {
		LoginDTO loginDto = new LoginDTO();
	    loginDto.setU_id(userId);
	    loginDto.setU_password(userPw);

	    LoginDTO login = service.userLogin(loginDto);

	    if (login == null) {
	        return "fail"; // 아이디가 존재하지 않음
	    }

	    String encodedPw = service.pwCheck(userId);
	    if (encodedPw == null || !pwdEncoder.matches(userPw, encodedPw)) {
	        return "fail"; // 비밀번호 불일치
	    }

	    // 로그인 성공
	    session.setAttribute("userId", login.getU_id());
	    session.setAttribute("userNicname", login.getU_nicname());
	    return "success";
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
