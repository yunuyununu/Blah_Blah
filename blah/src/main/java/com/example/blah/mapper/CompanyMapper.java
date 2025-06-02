package com.example.blah.mapper;

import java.util.List;
import java.util.Map;

import com.example.blah.domain.CompanyDTO;

public interface CompanyMapper {
	
	// 회사 목록
	List<CompanyDTO> list(int limit, int offset);
	
	// 게시물 상세
	Map<String, Object> companyDetails(int c_idx);
	
	// 회사 리뷰 목록
	List<CompanyDTO> reviewList(Map<String, Object> map);
	
	// 회사 리뷰 총 개수
	int reviewCount(int r_c_idx);
	
	// 회사 평균 별점
	Double companyStar(int r_c_idx);
	
	// 회사 신청
	void companyInsert(Map<String, Object> map);
	
	// 회사 순위
	List<CompanyDTO> companyScore();
}
