package com.example.blah.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.blah.domain.AlarmDTO;
import com.example.blah.mapper.AlarmMapper;
import com.example.blah.service.AlarmService;

@Service
public class AlarmServiceImpl implements AlarmService {
	
	@Autowired
	AlarmMapper alarmmapper;
	
	private SimpMessagingTemplate messagingTemplate;

    public AlarmServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }	

    public void sendLikeNotification(int receiverId, String message) {
    	try {
            // 알림 데이터 구성
            Map<String, Object> alarmData = new HashMap<>();
            alarmData.put("type", "LIKE");
            alarmData.put("message", message);
            alarmData.put("receiverId", receiverId);
            alarmData.put("timestamp", System.currentTimeMillis());
            
            System.out.println("서버에서 알림 전송: " + alarmData);
            
            // 개인 사용자에게 알림 전송
            messagingTemplate.convertAndSendToUser(
                String.valueOf(receiverId),
                "/queue/alarms",
                alarmData
            );
            
            System.out.println("알림 전송 완료 - 수신자: " + receiverId);
            
        } catch (Exception e) {
            System.err.println("알림 전송 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public List<AlarmDTO> alarmList(int a_u_idx) {
    	System.out.println("a_u_idx"+a_u_idx);
    	System.out.println("알림 리스트 : "+alarmmapper.alarmList(a_u_idx));
    	return alarmmapper.alarmList(a_u_idx);
    }
    
    @Override
    public void isRead(int a_u_idx) {
    	alarmmapper.isRead(a_u_idx);
    }
}
