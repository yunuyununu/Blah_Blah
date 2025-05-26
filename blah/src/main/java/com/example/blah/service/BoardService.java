package com.example.blah.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.example.blah.domain.BoardDTO;

public interface BoardService {
	List<BoardDTO> getBoard(Long lastBIdx);
	
	Map<String, Object> details(int b_idx);
	
	void incrementHit(int b_idx);
	
	void boardInsert(BoardDTO dto);
	
	// 이미지 임시저장 업로드
	void uploadImage(MultipartFile file, int u_idx) throws IOException;
	
	void confirmImageUsage(List<Integer> imageIds);
	
	void cleanupOldTempImages();
}
