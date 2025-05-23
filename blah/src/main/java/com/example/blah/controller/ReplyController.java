package com.example.blah.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blah.domain.BoardDTO;
import com.example.blah.domain.ReplyDTO;
import com.example.blah.service.BoardService;
import com.example.blah.service.ReplyService;

@RestController
@RequestMapping("reply/*")
public class ReplyController {

	@Autowired
	ReplyService service;
	
	
	// 게시판 목록
	@GetMapping("list")
	public List<ReplyDTO> getReplyList(@RequestParam(name = "cmBIdx") int cm_b_idx) {
		System.out.println("cmBIdx=>"+cm_b_idx);
		System.out.println("댓글 리스트=="+service.replyList(cm_b_idx));
	    return service.replyList(cm_b_idx);
	}
	
	//게시글 상세
//	@GetMapping("details")
//	public Map<String, Object> boardDetails(@RequestParam(name = "b_idx") int b_idx) {
//	    return service.details(b_idx);
//	}
	
	
}
