package com.example.blah.service;

import java.util.List;

import com.example.blah.domain.BoardDTO;

public interface MypageService {
	List<BoardDTO> getBoard(Long lastBIdx);

}
