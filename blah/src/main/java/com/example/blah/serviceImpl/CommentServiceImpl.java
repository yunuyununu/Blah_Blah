package com.example.blah.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blah.domain.CommentDTO;
import com.example.blah.mapper.CommentMapper;
import com.example.blah.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	
	@Autowired
	private CommentMapper commentMapper;
	
	@Override
	public List<CommentDTO> commentList(int cm_b_idx) {
		return commentMapper.commentList(cm_b_idx);
	}
	
	@Override
	public void commentInsert(CommentDTO dto) {
		commentMapper.commentInsert(dto);
	}
	
	@Override
	public void replyInsert(CommentDTO dto) {
		commentMapper.replyInsert(dto);
	}
	
	
}
