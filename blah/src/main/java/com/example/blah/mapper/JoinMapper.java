package com.example.blah.mapper;

import com.example.blah.domain.JoinDTO;

public interface JoinMapper {

	// 아이디 중복체크
	int idCheck(String U_ID);
	
	// 회원가입
	void userjoin(JoinDTO dto);
}
