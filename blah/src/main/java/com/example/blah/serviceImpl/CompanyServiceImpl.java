package com.example.blah.serviceImpl;

import java.util.List;

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
	public List<CompanyDTO> list() {
		return companyMapper.list();
	}
}
