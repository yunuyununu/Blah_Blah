package com.example.blah.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blah.mapper.JoinMapper;
import com.example.blah.service.JoinService;

@Service
public class JoinServiceImpl implements JoinService {
	
	@Autowired
	 private JoinMapper joinMapper;
	
//	 public JoinServiceImpl(JoinMapper joinMapper) {
//	        this.joinMapper = joinMapper;
//	 }
	
	@Override
	public int idCheck(String U_ID) {
		return joinMapper.idCheck(U_ID);
	}
}
