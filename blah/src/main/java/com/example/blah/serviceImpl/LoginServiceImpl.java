package com.example.blah.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blah.domain.LoginDTO;
import com.example.blah.mapper.LoginMapper;
import com.example.blah.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	 private LoginMapper loginMapper;
	
	@Override
	public LoginDTO userLogin(LoginDTO dto) {
		return loginMapper.userLogin(dto);
	}
	
	@Override
	public String pwCheck(String U_ID) {
		return loginMapper.pwCheck(U_ID);
	}
}
