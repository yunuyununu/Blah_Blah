package com.example.blah.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
	
	// 회원 전회사 파일 삭제(를 위한 조회)
	@Override
	public String myCompanyFile(int u_idx) {
		return mypageMapper.myCompanyFile(u_idx);
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
	public void userWithdraw(Map<String, Object> map) {
		mypageMapper.userWithdraw(map);
	}
	
	// 현회사 리뷰작성유무체크
	@Override
	public Map<String, Object> reviewCheck(int u_idx) {
		return mypageMapper.reviewCheck(u_idx);
	}
	
	// 내 리뷰 목록
	@Override
	public List<CompanyDTO> myreviewList(int u_idx) {
		return mypageMapper.myreviewList(u_idx);
	}
	
	// 리뷰 작성
	public void reviewInsert(Map<String, Object> map) {
		mypageMapper.reviewInsert(map);
	}
	
	// 리뷰 열람 처리 가능
	public void reviewAccessible(int u_idx) {
		mypageMapper.reviewAccessible(u_idx);
	}
	
	// 리뷰 수정
	@Override
	public void reviewUpdate(Map<String, Object> map) {
		mypageMapper.reviewUpdate(map);
	}
	
	// 리뷰 삭제
	@Override
	public void reviewDelete(int r_idx) {
		mypageMapper.reviewDelete(r_idx);
	}
	
	// 리뷰 갯수 카운트
	@Override
	public int reviewYN(int u_idx) {
		return mypageMapper.reviewYN(u_idx);
	}
	
	// 리뷰 작성 가능 여부 / U_REVIEW 컬럼
	@Override
	public void reviewStatusUpdate(int u_idx) {
		mypageMapper.reviewStatusUpdate(u_idx);
	}
	
	// 이메일 수정
	@Override
	public void emailChange(Map<String, Object> map) {
		mypageMapper.emailChange(map);
	}
}
