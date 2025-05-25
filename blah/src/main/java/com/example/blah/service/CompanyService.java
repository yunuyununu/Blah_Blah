package com.example.blah.service;

import java.util.List;
import java.util.Map;

import com.example.blah.domain.CompanyDTO;

public interface CompanyService {
	List<CompanyDTO> list(int limit, int offset);
	
	Map<String, Object> details(int c_idx);
}
