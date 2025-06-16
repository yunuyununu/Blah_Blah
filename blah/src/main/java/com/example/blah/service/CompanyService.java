package com.example.blah.service;

import java.util.List;
import java.util.Map;

import com.example.blah.domain.CompanyDTO;

public interface CompanyService {
	
	List<Map<String, Object>> list(String searchKeyword,int page, int offset);
	
	int companyTotalCount(String searchKeyword);
	
	Map<String, Object> details(int c_idx);
	
	List<CompanyDTO> reviewList(int r_c_idx,int limit, int offset);
	
	int reviewCount(int r_c_idx);
	
	Double companyStar(int r_c_idx);
	
	void companyInsert(Map<String, Object> map);
	
	// 회사 순위
	List<CompanyDTO> companyScore();
	
	// 리뷰 존재유무
	int reviewYN(int r_u_idx);
}