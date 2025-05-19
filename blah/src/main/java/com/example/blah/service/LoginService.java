package com.example.blah.service;

import com.example.blah.domain.LoginDTO;

public interface LoginService {
	
	// 회원로그인
	LoginDTO userLogin(LoginDTO dto);
	//int userLogin(String U_ID, String U_PASSWORD);
	// 비밀번호 체크
	String pwCheck(String U_ID);
}
