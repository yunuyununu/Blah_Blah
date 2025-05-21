package com.example.blah.service;

import com.example.blah.domain.JoinDTO;

public interface JoinService {
	
	// 아이디 중복체크
	int idCheck(String U_ID);
	
	// 회원가입
	String join(JoinDTO dto);
}
