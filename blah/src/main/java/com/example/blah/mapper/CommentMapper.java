package com.example.blah.mapper;

import java.util.List;

import com.example.blah.domain.CommentDTO;

public interface CommentMapper {
	// 댓글 리스트
	List<CommentDTO> commentList(int cm_b_idx);
	
	// 댓글 등록
	void commentInsert(CommentDTO dto);
	
	// 대댓글 등록
	void replyInsert(CommentDTO dto);
}
