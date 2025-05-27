package com.example.blah.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blah.domain.CommentDTO;
import com.example.blah.service.CommentService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("reply/*")
public class CommentController {

	@Autowired
	CommentService service;
	
	
	// 게시판 목록
	@GetMapping("list")
	public List<CommentDTO> getCommentList(@RequestParam(name = "cmBIdx") int cm_b_idx) {
	    return service.commentList(cm_b_idx);
	}
	
	//게시글 상세
//	@GetMapping("details")
//	public Map<String, Object> boardDetails(@RequestParam(name = "b_idx") int b_idx) {
//	    return service.details(b_idx);
//	}
	
	// 댓글 입력
	@PostMapping("commentInsert")
	public String commentInsert(@RequestBody Map<String, String> request, HttpSession session) {
		String result = "";
		try {
			CommentDTO dto = new CommentDTO();
			dto.setCm_u_idx((int) session.getAttribute("UserIdx"));
			dto.setCm_b_idx(Integer.parseInt(request.get("cm_b_idx")));
			dto.setCm_content(request.get("cm_content"));
			service.commentInsert(dto);
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		return result;
	}
	
	// 대댓글 입력
	@PostMapping("replyInsert")
	public String replyInsert(@RequestBody Map<String, String> request, HttpSession session) {
		String result = "";
		try {
			CommentDTO dto = new CommentDTO();
			dto.setCm_u_idx((int) session.getAttribute("UserIdx"));
			dto.setCm_b_idx(Integer.parseInt(request.get("cm_b_idx")));
			dto.setCm_content(request.get("cm_content"));
			dto.setCm_parent_idx(request.get("cm_parent_idx"));
			service.replyInsert(dto);
			result = "success";
		} catch (Exception e) {
			e.printStackTrace();
			result = "fail";
		}
		return result;
	}
}
