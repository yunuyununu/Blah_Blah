import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;

export const getStompClient = () => stompClient;

export const connectWebSocket = (userId, onMessageCallback) => {
  stompClient = new Client({
    webSocketFactory: () => new SockJS('http://localhost:80/ws'),
    reconnectDelay: 5000,
    onConnect: () => {
      console.log('웹소켓 연결 성공');

      stompClient.subscribe(`/user/${userId}/queue/alarms`, (message) => {
        const payload = JSON.parse(message.body);
        console.log('🔔 알림 수신:', payload);
        onMessageCallback(payload);
      });

      if (typeof onMessageCallback === 'function') {
        onMessageCallback({ type: 'CONNECTED' }); // 연결 완료 신호용 (필요시)
      }
    },
    onStompError: (frame) => {
      console.error('STOMP 오류:', frame.headers['message']);
      console.error('상세 오류:', frame.body);
    },
  });

  stompClient.activate();
};

export const disconnectWebSocket = () => {
  if (stompClient && stompClient.active) {
    stompClient.deactivate();
  }
};