package com.example.blah.service;

import java.util.List;

import com.example.blah.domain.ReplyDTO;

public interface ReplyService {
	List<ReplyDTO> replyList(int cm_b_idx);
}
