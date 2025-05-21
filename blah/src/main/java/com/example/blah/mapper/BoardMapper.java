package com.example.blah.mapper;

import java.util.List;
import java.util.Map;

import com.example.blah.domain.BoardDTO;

public interface BoardMapper {
	// 처음 게시글
	List<BoardDTO> firstList();
	
	// 더보기 요청 시 게시글
	List<BoardDTO> plusList(Long lastBIdx);
	
	// 게시물 상세
	Map<String, Object> boardDetails(int b_idx);
	
	// 조회수 증가
	void updateHit(int b_idx);
	
	// 게시물 등록
	void boardInsert(BoardDTO dto);
}
