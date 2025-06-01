import { defineStore } from 'pinia';
import { connectWebSocket, disconnectWebSocket } from '@/plugin/websocket';

export const useAlarmStore = defineStore('alarm', {
  state: () => ({
    alarms: [],
    hasNew: false,
    connected: false
  }),
  actions: {
    init(userId) {
      connectWebSocket(userId, this.onMessage);
    },
    onMessage(payload) {
      this.alarms.unshift(payload);
      this.hasNew = true;
    },
    markAllRead() {
      this.alarms = this.alarms.map(alarm => ({ ...alarm, A_READ: 'Y' }));
      this.hasNew = false;
    },
    connect(userId, callback) {
      if (this.connected) return;

      connectWebSocket(userId, (payload) => {
        this.connected = true;
        callback(payload);
      });
    },
    disconnect() {
      disconnectWebSocket();
      this.connected = false;
    }
  }
});