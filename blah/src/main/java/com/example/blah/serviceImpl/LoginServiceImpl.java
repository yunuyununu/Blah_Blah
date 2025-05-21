package com.example.blah.serviceImpl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.blah.domain.LoginDTO;
import com.example.blah.mapper.LoginMapper;
import com.example.blah.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	LoginMapper loginMapper;
	
	@Autowired
	PasswordEncoder pwdEncoder;
	
	// 회원 로그인 -> 비밀번호 인코딩 되지 않은 패스워드 일치 여부 확인
	@Override
	public LoginDTO userLogin(String U_ID,String U_PASSWORD) {
		LoginDTO login = loginMapper.userLogin(U_ID);
		if (login != null && pwdEncoder.matches(U_PASSWORD, login.getU_password())) {
			//System.out.println(U_PASSWORD); // 입력한 비밀번호
			//System.out.println(dto.getU_password()); // db에 저장된 암호화 비밀번호
			return login;
		}
		return null;
	}
	
	// 아이디 찾기
	@Override
	public LoginDTO searchId(Map<String, String> map) {
		return loginMapper.searchId(map);
		
	}
		
	// 비밀번호 찾기 정보 검증
	@Override
	public LoginDTO searchPw(Map<String, String> map) {
		return loginMapper.searchPw(map);
	}
	
	
	// 비밀번호 재설정
	@Override
	public String pwReset(LoginDTO dto) {
		String result = "";
		try {
			loginMapper.pwReset(dto);
			result ="success";
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		return result;
	}
	

}
