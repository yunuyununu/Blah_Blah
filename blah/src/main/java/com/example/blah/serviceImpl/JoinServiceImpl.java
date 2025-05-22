package com.example.blah.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blah.domain.JoinDTO;
import com.example.blah.mapper.JoinMapper;
import com.example.blah.service.JoinService;

@Service
public class JoinServiceImpl implements JoinService {
	
	@Autowired
	 private JoinMapper joinMapper;
	
	@Override
	public int idCheck(String U_ID) {
		return joinMapper.idCheck(U_ID);
	}
	
	@Override
	public String join(JoinDTO dto) {
		String result = "";
		
		try {
			joinMapper.userjoin(dto);
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		
		return result;
	}
	
	@Override
	public int emailCheck(String U_EMAIL) {
		return joinMapper.emailCheck(U_EMAIL);
	}
	
	
}
