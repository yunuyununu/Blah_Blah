package com.example.blah.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.example.blah.domain.BoardDTO;

public interface BoardService {
	// 게시물 목록
	List<BoardDTO> getBoard(Long lastBIdx);
	
	// 게시물 상세
	Map<String, Object> details(int b_idx);
	
	// 게시물 상세 - 이미지파일
	List<Map<String, Object>> boardImages(int b_idx);
	
	// 게시물 조회수
	void incrementHit(int b_idx);
	
	// 게시물 등록
	void boardInsert(int u_idx, String title, String content, List<MultipartFile> images) throws IOException;
}
