package com.example.blah.service;

import java.util.List;
import java.util.Map;

import com.example.blah.domain.CompanyDTO;

public interface MypageService {
	
	// 마이페이지
	Map<String, Object> mypage(int u_idx);
	
	// 회원 회사 변경
	void companyChange(Map<String, Object> map);
	
	// 회원 전회사 파일 삭제(를 위한 조회)
	String myCompanyFile(int u_idx);
	
	// 휴대전화번호 변경
	void phoneUpdate(Map<String, Object> map);
	
	// 닉네임 변경
	void nicnameUpdate(Map<String, Object> map);
	
	// 회원 탈퇴
	void userWithdraw(Map<String, Object> map);
	
	// 리뷰작성유무체크
	Map<String, Object> reviewCheck(int u_idx);
	
	// 내 리뷰 목록
	List<CompanyDTO> myreviewList(int u_idx);
	
	// 리뷰 작성
	void reviewInsert(Map<String, Object> map);
	
	// 리뷰 열람 가능 처리
	void reviewAccessible(int u_idx);
	
	// 리뷰 수정
	void reviewUpdate(Map<String, Object> map);

	// 리뷰 삭제
	void reviewDelete(int r_idx);
	
	// 리뷰갯수 카운트
	int reviewYN(int u_idx);
	
	// 회원 현회사 리뷰 존재 유무
	void reviewStatusUpdate(int u_idx);
}
