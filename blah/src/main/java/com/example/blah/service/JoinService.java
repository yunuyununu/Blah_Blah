package com.example.blah.service;

import com.example.blah.domain.JoinDTO;

public interface JoinService {
	int idCheck(String U_ID);
	String join(JoinDTO dto);
}
