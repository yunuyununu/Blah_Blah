package com.example.blah.service;

import java.util.Map;

import com.example.blah.domain.LoginDTO;

public interface LoginService {
	
	// 회원로그인
	LoginDTO userLogin(String U_ID, String U_PASSWORD);
	
	// 아이디 찾기
	LoginDTO searchId(Map<String, String> map);
	
	// 비밀번호 찾기 정보 검증
	LoginDTO searchPw(Map<String, String> map);
	
	// 비밀번호 재설정
	String pwReset(LoginDTO dto);
}
