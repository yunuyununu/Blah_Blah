package com.example.blah.service;

import java.util.List;
import java.util.Map;

import com.example.blah.domain.BoardDTO;

public interface BoardService {
	List<BoardDTO> getBoard(Long lastBIdx);
	
	Map<String, Object> details(int b_idx);
	
	void incrementHit(int b_idx);
	
	void boardInsert(BoardDTO dto);
}
