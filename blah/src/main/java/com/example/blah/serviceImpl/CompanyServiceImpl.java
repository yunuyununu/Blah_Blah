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
	public List<Map<String, Object>> list(String searchKeyword,int page, int offset) {
		System.out.println("여기확인해=>"+companyMapper.list(searchKeyword, page, offset));

		return companyMapper.list(searchKeyword, page,offset);
	}
	
	// 게시물 총 갯수
	@Override
	public int companyTotalCount(String searchKeyword) {
		return companyMapper.companyTotalCount(searchKeyword);
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
	
	// 리뷰 존재 유무
	@Override
	public int reviewYN(int r_u_idx) {
		return companyMapper.reviewYN(r_u_idx);
	}
}
