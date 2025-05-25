package com.example.blah.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blah.domain.CompanyDTO;
import com.example.blah.mapper.CompanyMapper;
import com.example.blah.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {
	
	@Autowired
	 private CompanyMapper companyMapper;
	
	@Override
	public List<CompanyDTO> list(int limit, int offset) {
		return companyMapper.list(limit, offset);
	}
	
	@Override
	public Map<String, Object> details(int c_idx) {
		return companyMapper.companyDetails(c_idx);
	}
}
