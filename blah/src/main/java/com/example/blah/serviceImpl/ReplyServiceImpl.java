package com.example.blah.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blah.domain.ReplyDTO;
import com.example.blah.mapper.ReplyMapper;
import com.example.blah.service.ReplyService;

@Service
public class ReplyServiceImpl implements ReplyService {
	
	@Autowired
	private ReplyMapper replyMapper;
	
	@Override
	public List<ReplyDTO> replyList(int cm_b_idx) {
		return replyMapper.replyList(cm_b_idx);
	}
	
}
