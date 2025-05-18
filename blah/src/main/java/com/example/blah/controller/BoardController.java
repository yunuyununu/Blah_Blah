package com.example.blah.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blah.domain.BoardDTO;
import com.example.blah.service.BoardService;

@RestController
@RequestMapping("board/*")
public class BoardController {

	@Autowired
	BoardService service;
	
	@RequestMapping("list")
	public List<BoardDTO> list() {
		//@RequestParam(name="") String searchkey, @RequestParam(name="") String search
//		int count = dao.count(); // 레코드 개수
//		PageUtil page = new PageUtil(count, curPage);
//		int start = page.getPageBegin();
//		int end = page.getPageEnd();
//		List<BoardDTO> list = dao.list(start, end);
//		List<BoardDTO> list;
//
//		
//		Map<String, Object> map = new HashMap<>();
//		map.put("searchkey", searchkey);
//		map.put("search", search);
//		List<BoardDTO> list = dao.list(searchkey, search);
		List<BoardDTO> list = service.list();
		System.out.print("list==="+list);
		return list;
	}
}
