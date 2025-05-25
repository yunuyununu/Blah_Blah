package com.example.blah.mapper;

import java.util.List;
import java.util.Map;

import com.example.blah.domain.CompanyDTO;

public interface CompanyMapper {
	
	// 회사 목록
	List<CompanyDTO> list(int limit, int offset);
	
	// 게시물 상세
	Map<String, Object> companyDetails(int c_idx);
}
