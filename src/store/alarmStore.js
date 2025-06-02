import { defineStore } from 'pinia';
import { connectWebSocket, disconnectWebSocket } from '@/plugin/websocket';

export const useAlarmStore = defineStore('alarm', {
  state: () => ({
    alarms: [],
    hasNew: false,
    connected: false,
    connecting: false
  }),
  actions: {
    init(userId) {
      connectWebSocket(userId, this.onMessage);
    },
    onMessage(payload) {
      if (payload.type === 'CONNECTED') {
        this.connected = true;
        this.connecting = false;
        console.log('✅ 웹소켓 연결 완료');
        return;
      }
      
      // 실제 알림 메시지 처리
      console.log('🔔 새 알림 수신:', payload);
      this.alarms.unshift(payload);
      this.hasNew = true;
    },
    markAllRead() {
      this.alarms = this.alarms.map(alarm => ({ ...alarm, A_READ: 'Y' }));
      this.hasNew = false;
    },
    connect(userId, callback) {
      // 이미 연결되어 있거나 연결 중이면 리턴
      if (this.connected || this.connecting) {
        console.log('이미 웹소켓이 연결되어 있습니다.');
        return;
      }

      console.log('웹소켓 연결 시도 중...', userId);
      this.connecting = true;

      connectWebSocket(userId, (payload) => {
        this.onMessage(payload);
        
        // 콜백 함수가 있으면 실행
        if (typeof callback === 'function') {
          callback(payload);
        }
      });
    },
    disconnect() {
      disconnectWebSocket();
      this.connected = false;
      this.connecting = false;
      console.log('웹소켓 연결 해제됨');
    }
  }
});