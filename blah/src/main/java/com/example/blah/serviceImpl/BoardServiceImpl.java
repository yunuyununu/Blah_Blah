package com.example.blah.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.example.blah.controller.BoardController;
import com.example.blah.domain.BoardDTO;
import com.example.blah.mapper.BoardMapper;
import com.example.blah.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	BoardMapper boardMapper;

	@Override
	public List<BoardDTO> getBoard(Long lastBIdx) {
	    if (lastBIdx == null) {
	        return boardMapper.firstList();
	    } else {
	        return boardMapper.plusList(lastBIdx);
	    }
	}
	
	@Override
	public Map<String, Object> details(int b_idx) {
		return boardMapper.boardDetails(b_idx);
	}
	
	@Override
	public void incrementHit(int b_idx) {
		boardMapper.updateHit(b_idx);
	}
	
	@Override
	public void boardInsert(BoardDTO dto) {
//		int idx = dto.getB_idx();
//		
//		try {
//			if (CollectionUtils.isEmpty(files)) {
				boardMapper.boardInsert(dto);
//			} else {
//				for (BoardDTO file : files) {
//					file.setI_idx(idx);
//				}
//				boardMapper.boardInsert(dto);
//				boardMapper.saveAll(files);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("게시글 저장 실패!!!");
//		}
		
	}
	
}
