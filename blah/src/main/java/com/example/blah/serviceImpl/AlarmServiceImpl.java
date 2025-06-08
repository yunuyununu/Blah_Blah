package com.example.blah.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.blah.domain.AlarmDTO;
import com.example.blah.mapper.AlarmMapper;
import com.example.blah.service.AlarmService;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class AlarmServiceImpl implements AlarmService {
	
	@Autowired
	AlarmMapper alarmmapper;
	
	private final ConcurrentHashMap<Integer, SseEmitter> emitters = new ConcurrentHashMap<>();
	
	@Autowired
	private ObjectMapper objectMapper;

	public SseEmitter subscribe(int userId) {
        System.out.println("=== SSE 구독 처리 ===");
        System.out.println("사용자 ID: " + userId);
        System.out.println("현재 연결된 emitters: " + emitters.keySet());
        
        // 기존 연결이 있다면 종료
        SseEmitter oldEmitter = emitters.get(userId);
        if (oldEmitter != null) {
            System.out.println("기존 연결 종료: " + userId);
            oldEmitter.complete();
            emitters.remove(userId);
        }
        
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60); // 60분 유지
        emitters.put(userId, emitter);
        
        emitter.onCompletion(() -> {
            System.out.println("SSE 연결 완료됨 - 사용자 ID: " + userId);
            emitters.remove(userId);
        });
        
        emitter.onTimeout(() -> {
            System.out.println("SSE 연결 타임아웃 - 사용자 ID: " + userId);
            emitters.remove(userId);
        });
        
        emitter.onError((e) -> {
            System.out.println("SSE 연결 오류 - 사용자 ID: " + userId + ", 오류: " + e.getMessage());
            emitters.remove(userId);
        });
        
        System.out.println("새 SSE 연결 생성됨 - 사용자 ID: " + userId);
        System.out.println("현재 총 연결 수: " + emitters.size());
        
        return emitter;
    }

	public void sendNotification(int receiverId, String message) {
        System.out.println("=== 알림 전송 시도 ===");
        System.out.println("수신자 ID: " + receiverId);
        System.out.println("메시지: " + message);
        System.out.println("현재 연결된 사용자들: " + emitters.keySet());
        
        try {
            SseEmitter emitter = emitters.get(receiverId);
            if (emitter != null) {
                System.out.println("수신자의 SSE 연결 찾음");
                System.out.println("Emitter 상태: " + emitter.toString());

                try {
                    // JSON 형태로 데이터 구성
                    Map<String, Object> notificationData = new HashMap<>();
                    notificationData.put("message", message);
                    notificationData.put("type", "like");
                    notificationData.put("timestamp", System.currentTimeMillis()); // 타임스탬프 추가
                    
                    // JSON 문자열로 변환 (간단한 방식)
                    String jsonData = objectMapper.writeValueAsString(notificationData);
                    System.out.println("전송할 JSON 데이터: " + jsonData);
                    
                    emitter.send(SseEmitter.event()
                        .name("like")
                        .data(jsonData));
                    
                    System.out.println("알림 전송 성공: " + jsonData);
                } catch (Exception e) {
                    System.err.println("SSE 전송 중 오류 발생: " + e.getMessage());
                    e.printStackTrace();
                    emitters.remove(receiverId);
                }
            } else {
                System.out.println("수신자 " + receiverId + "의 SSE 연결을 찾을 수 없음");
                System.out.println("현재 연결된 사용자 목록: " + emitters.keySet());
            }
        } catch (Exception e) {
            System.err.println("알림 전송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
	@Override
    public List<AlarmDTO> alarmList(int a_u_idx) {
        System.out.println("a_u_idx: " + a_u_idx);
        System.out.println("알림 리스트: " + alarmmapper.alarmList(a_u_idx));
        return alarmmapper.alarmList(a_u_idx);
    }
    
    @Override
    public void isRead(int a_u_idx) {
    	alarmmapper.isRead(a_u_idx);
    }
}
