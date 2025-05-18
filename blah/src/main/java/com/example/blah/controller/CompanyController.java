package com.example.blah.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blah.domain.CompanyDTO;
import com.example.blah.service.CompanyService;

@RestController
@RequestMapping("company/*")
public class CompanyController {
	
	@Autowired
	CompanyService service;
	
	@RequestMapping("list")
	public List<CompanyDTO> list() {
		List<CompanyDTO> list = service.list();
		return list;
	}
}
