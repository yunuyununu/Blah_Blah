package com.example.blah.serviceImpl;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.blah.domain.MessageDTO;
import com.example.blah.service.AlarmService;

@Service
public class AlarmServiceImpl implements AlarmService {
	
	private SimpMessagingTemplate messagingTemplate;

    public AlarmServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }	

    public void sendLikeNotification(int receiverId, String message) {
    	//MessageDTO notification = new MessageDTO(message, receiverId);
        messagingTemplate.convertAndSend("/topic/notifications/" + receiverId, message);
    }
}
