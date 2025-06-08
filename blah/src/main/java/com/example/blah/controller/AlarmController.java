package com.example.blah.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

    @GetMapping(value = "subscribe/{userIdx}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable("userIdx") int userIdx) {
        System.out.println("=== SSE 구독 요청 ===");
        System.out.println("요청된 userIdx: " + userIdx);
        
        
        SseEmitter emitter = alarmservice.subscribe(userIdx);
        System.out.println("SSE Emitter 생성됨: " + emitter);
        
        // 연결 확인을 위한 초기 메시지 전송
        try {
            emitter.send(SseEmitter.event()
                .name("like")
                .data("SSE 연결이 성공적으로 설정되었습니다."));
            System.out.println("초기 연결 메시지 전송 완료");
        } catch (Exception e) {
            System.err.println("초기 메시지 전송 실패: " + e.getMessage());
        }
        
        return emitter;
    }
}
