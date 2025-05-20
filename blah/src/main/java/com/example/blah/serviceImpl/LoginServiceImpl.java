package com.example.blah.serviceImpl;

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
	
	@Override
	public LoginDTO userLogin(String U_ID,String U_PASSWORD) {
		LoginDTO dto = loginMapper.userLogin(U_ID);
		if (dto != null && pwdEncoder.matches(U_PASSWORD, dto.getU_password())) {
			System.out.println(U_PASSWORD);
			System.out.println(dto.getU_password());
			return dto;
		}
		return null;
	}
	
	@Override
	public String pwCheck(String U_ID) {
		return loginMapper.pwCheck(U_ID);
	}
}
