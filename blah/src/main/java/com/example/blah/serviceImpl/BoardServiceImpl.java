package com.example.blah.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.blah.domain.BoardDTO;
import com.example.blah.mapper.BoardMapper;
import com.example.blah.service.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	 private BoardMapper boardMapper;
	
	@Override
	public List<BoardDTO> list() {
//		Map<String, Object> map = new HashMap<>();
//		map.put("searchkey", searchkey);
//		map.put("search", search);
		return boardMapper.list();
	}
	
//	public count() {
//		return sqlSession.selectOne("");
//	}
	
//	@Override
//	public int countRecord(Map<String, Object> map) {
//		return sqlSession.selectOne("order.countRecord", map);
//	}
}
