package com.example.blah.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blah.domain.CompanyDTO;
import com.example.blah.mapper.MypageMapper;
import com.example.blah.service.MypageService;

@Service
public class MypageServiceImpl implements MypageService {
	
	@Autowired
	MypageMapper mypageMapper;

	@Override
	public Map<String, Object> mypage(int u_idx) {
		return mypageMapper.mypage(u_idx);
	}
	
	// 회원 회사 변경
	@Override
	public void companyChange(Map<String, Object> map) {
		mypageMapper.companyChange(map);
	}
	
	// 휴대전화번호 변경
	@Override
	public void phoneUpdate(Map<String, Object> map) {
		mypageMapper.phoneUpdate(map);
	}
	
	// 닉네임 변경
	@Override
	public void nicnameUpdate(Map<String, Object> map) {
		mypageMapper.nicnameUpdate(map);
	}
	
	// 회원 탈퇴
	@Override
	public void userWithdraw(int u_idx) {
		mypageMapper.userWithdraw(u_idx);
	}
	
	// 내 리뷰 목록
	@Override
	public List<CompanyDTO> myreview(Map<String, Object> map) {
		return mypageMapper.myreview(map);
	}
}
