package com.example.blah.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.blah.domain.AlarmDTO;
import com.example.blah.service.AlarmService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("alarm/*")
public class AlarmController {
	
	@Autowired
	AlarmService alarmservice;
	
	@GetMapping("list")
	public List<AlarmDTO> getAlarms(HttpSession session) {
	    int userId = (int)session.getAttribute("UserIdx"); // 로그인 사용자 ID
	    System.out.println("컨트롤러에서 알림리스ㅡ=>"+alarmservice.alarmList(userId));
	    return alarmservice.alarmList(userId);
	}
	
	@PostMapping("isRead")
	public void isRead(HttpSession session) {
		int userId = (int)session.getAttribute("UserIdx");
		alarmservice.isRead(userId);
	}
}
