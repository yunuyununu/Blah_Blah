package com.example.blah.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.blah.domain.CompanyDTO;
import com.example.blah.service.CompanyService;

@RestController
@RequestMapping("company/*")
public class CompanyController {
	
	@Autowired
	CompanyService service;
	
	@GetMapping("list")
	public List<CompanyDTO> list(@RequestParam(name = "limit") int limit, @RequestParam(name = "offset") int offset) {
		List<CompanyDTO> list = service.list(limit, offset);
		return list;
	}
	
	// 게시글 상세
	@GetMapping("details")
	public Map<String, Object> companyDetails(@RequestParam(name = "c_idx") int c_idx) {
	    return service.details(c_idx);
	}
		
}
