package com.example.blah.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.example.blah.domain.BoardDTO;
import com.example.blah.domain.VoteDTO;

import jakarta.servlet.http.HttpSession;

public interface BoardService {
	// 게시물 목록
	List<Map<String, Object>> getBoard(String searchKeyword,int page, int offset);
	
	// 게시물 총 갯수
	int getBoardTotalCount(String searchKeyword);
	
	// 게시물 상세
	Map<String, Object> details(int b_idx);
	
	// 게시물 상세 - 이미지파일
	List<Map<String, Object>> boardImages(int b_idx);
	
	// 게시물 조회수
	void incrementHit(int b_idx);
	
	// 좋아요 중복체크
	int likePrevent (Map<String, Object> map);
	
	// 좋아요 입력
	void boardLike(Map<String, Object> map);
	
	// 좋아요 취소
	void likeDelete(Map<String, Object> map);
	
	// 게시물 등록
	int boardInsert(int u_idx, String title, String content, List<MultipartFile> images) throws IOException;

	// 게시물 수정 시
	void boardUpdate(int u_idx, String b_title, String b_content);
	
	// 게시물 수정 시 (첨부파일 변경)
	void boardImageUpdate(int b_idx,String b_title, String b_content, List<MultipartFile> images) throws IOException;
	
	// 게시물 이미지 삭제
	void imageDelete(String i_image);
	
	// 게시물 삭제 시
	void boardDelete(int b_idx);
	
	// 하트 갯수
	int heartCount(int b_idx);
	
	// 주간 토픽 베스트
	List<BoardDTO> weeklyBest();
		
	// 월간 토픽 베스트
	List<BoardDTO> monthlyBest();
	
	// 투표 등록
	void voteInfoInsert(int v_b_idx, String v_title, List<String> options);
	
	// 투표 정보
	List<Map<String, Object>> voteInfo(int v_b_idx);
	
	// 투표한 항목상태
	int voteCheck(Map<String, Object> map);
	
	// 투표 선택
	void votePick(int vr_u_idx, int vr_vo_idx);
	
	// 메인 투표 베스트
	List<Map<String, Object>> voteMain();
}
