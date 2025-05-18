package com.example.blah.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login/*")
public class LoginController {
	
//	@Autowired
//	LoginDAO dao;
	
	@Autowired
	PasswordEncoder pwdEncoder;
	
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
