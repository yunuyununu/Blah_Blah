package com.example.blah.service;

import java.util.List;

import com.example.blah.domain.CommentDTO;

public interface CommentService {
	// 댓글 리스트
	List<CommentDTO> commentList(int cm_b_idx);
	
	// 댓글 등록
	void commentInsert(CommentDTO dto);
	
	// 대댓글 등록
	void replyInsert(CommentDTO dto);
	
	// 댓글/대댓글 삭제
	void commentDelete(int cm_idx);
	
	// 댓글/대댓글 수정
	void commentUpdate(CommentDTO dto);
}
