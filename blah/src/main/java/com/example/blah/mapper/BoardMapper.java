package com.example.blah.mapper;

import java.util.List;
import java.util.Map;

import com.example.blah.domain.AlarmDTO;
import com.example.blah.domain.BoardDTO;
import com.example.blah.domain.FileDTO;

public interface BoardMapper {
	// 처음 게시글
	List<BoardDTO> firstList();
	
	// 더보기 요청 시 게시글
	List<BoardDTO> plusList(Long lastBIdx);
	
	// 게시물 상세
	Map<String, Object> boardDetails(int b_idx);
	
	// 게시물 상세 - 이미지파일
	List<Map<String, Object>> boardImages(int b_idx);
	
	// 조회수 증가
	void updateHit(int b_idx);
	
	// 좋아요 중복체크
	int likePrevent (Map<String, Object> map);
	
	// 좋아요 입력
	void boardLike(Map<String, Object> map);
	
	// 좋아요 취소
	void likeDelete(Map<String, Object> map);
	
	// 알람
	void alarmInsert(AlarmDTO dto);
	
	// 게시물 등록
	void boardInsert(BoardDTO dto);
	
	// 게시물 등록 시 이미지 첨부 (다중)
	void imageInsert(FileDTO image);
	
	// 게시물 삭제 시
	void boardDelete(int b_idx);
	
	// 하트 갯수
	int heartCount(int b_idx);
	
}
