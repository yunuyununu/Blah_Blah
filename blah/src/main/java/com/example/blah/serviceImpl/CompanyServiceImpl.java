package com.example.blah.serviceImpl;

import java.util.HashMap;
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
	
	@Override
	public List<CompanyDTO> reviewList(int r_c_idx,int limit, int offset) {
		Map<String, Object> map = new HashMap<>();
		map.put("r_c_idx", r_c_idx);
		map.put("limit", limit);
		map.put("offset", offset);
		return companyMapper.reviewList(map);
	}
	
	@Override
	public int reviewCount(int r_c_idx) {
		return companyMapper.reviewCount(r_c_idx);
	}
	
	@Override
	public Double companyStar(int r_c_idx) {
		return companyMapper.companyStar(r_c_idx);
	}
	
	@Override
	public void companyInsert(Map<String, Object> map) {
		companyMapper.companyInsert(map);
	}
	
	// 회사 순위
	@Override
	public List<CompanyDTO> companyScore() {
		return companyMapper.companyScore();
	}
}
