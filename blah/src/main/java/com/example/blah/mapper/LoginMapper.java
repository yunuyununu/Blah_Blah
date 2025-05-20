package com.example.blah.mapper;

import com.example.blah.domain.LoginDTO;

public interface LoginMapper {
	
	// 회원로그인
	LoginDTO userLogin(String U_ID);
	
	// 비밀번호 체크
	String pwCheck(String U_ID);
}
