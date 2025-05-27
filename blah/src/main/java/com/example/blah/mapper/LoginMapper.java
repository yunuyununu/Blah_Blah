package com.example.blah.mapper;

import java.util.Map;

import com.example.blah.domain.LoginDTO;

public interface LoginMapper {
	
	// 회원로그인
	LoginDTO userLogin(String U_ID);
	
	// 회원 탈퇴 여부
	String userWithdrawYN(String U_ID);
	
	// 아이디 찾기
	LoginDTO searchId(Map<String, String> map);
	
	// 비밀번호 찾기 정보 검증
	LoginDTO searchPw(Map<String, String> map);
	
	// 비밀번호 재설정
	void pwReset(LoginDTO dto);
}
