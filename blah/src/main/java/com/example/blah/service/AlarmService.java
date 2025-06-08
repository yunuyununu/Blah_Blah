package com.example.blah.service;

import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.blah.domain.AlarmDTO;

public interface AlarmService {
	
	SseEmitter subscribe(int userId);
	
	void sendNotification(int receiverId, String message);
	
	List<AlarmDTO> alarmList(int a_u_idx);
	
	void isRead(int a_u_idx);
}
