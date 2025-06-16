package com.example.blah.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.example.blah.domain.CompanyDTO;

public interface CompanyMapper {
	
	// 회사 목록
	List<Map<String, Object>> list(@Param("searchKeyword") String searchKeyword,@Param("page") int page,@Param("offset") int offset);
	
	// 회사 총 갯수
	int companyTotalCount(String searchKeyword);
	
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
	
	// 회원의 리뷰 존재유무
	int reviewYN(int r_u_idx);
}
