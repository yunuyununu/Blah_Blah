package com.example.blah.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blah.domain.BoardDTO;
import com.example.blah.service.BoardService;

@RestController
@RequestMapping("reply/*")
public class ReplyController {

	@Autowired
	BoardService service;
	
	
	// 게시판 목록
	@GetMapping("boards")
	public List<BoardDTO> getBoardList(@RequestParam(name = "lastBIdx", required = false) Long lastBIdx) {
	    return service.getBoard(lastBIdx);
	}
	
	//게시글 상세
	@GetMapping("details")
	public Map<String, Object> boardDetails(@RequestParam(name = "b_idx") int b_idx) {
	    return service.details(b_idx);
	}
	
	
}
