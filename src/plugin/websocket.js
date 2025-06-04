// import { Client } from '@stomp/stompjs';
// import SockJS from 'sockjs-client';

let stompClient = null;
// let isConnected = false;
// let connectionPromise = null;

export const getStompClient = () => stompClient;

// export const isWebSocketConnected = () => {
//   return stompClient && 
//          stompClient.connected && 
//          stompClient.active && 
//          isConnected;
// };

// export const connectWebSocket = (userId, onMessageCallback) => {
//   // 이미 연결 중이면 해당 Promise 반환
//   if (connectionPromise) {
//     return connectionPromise;
//   }

//   // 기존 연결이 있으면 먼저 해제
//   if (stompClient && stompClient.active) {
//     console.log('기존 웹소켓 연결 해제 중...');
//     stompClient.deactivate();
//   }

//   isConnected = false;
//   console.log('새 웹소켓 연결 생성 중...', userId);

//   connectionPromise = new Promise((resolve, reject) => {
//     stompClient = new Client({
//       webSocketFactory: () => new SockJS('http://localhost:80/ws'),
//       reconnectDelay: 5000,
//       heartbeatIncoming: 4000,
//       heartbeatOutgoing: 4000,
      
//       onConnect: (frame) => {
//         console.log('✅ 웹소켓 서버 연결 성공', frame);
//         isConnected = true;

//         try {
//           // 개인 알림 채널 구독
//           const subscription = stompClient.subscribe(`/user/${userId}/queue/alarms`, (message) => {
//             try {
//               const payload = JSON.parse(message.body);
//               console.log('📨 알림 메시지 수신:', payload);
//               if (typeof onMessageCallback === 'function') {
//                 onMessageCallback(payload);
//               }
//             } catch (error) {
//               console.error('알림 메시지 파싱 오류:', error);
//             }
//           });

//           console.log('구독 완료:', subscription);

//           if (typeof onMessageCallback === 'function') {
//             onMessageCallback({ type: 'CONNECTED' });
//           }

//           connectionPromise = null; // 연결 완료 후 Promise 초기화
//           resolve(stompClient);
          
//         } catch (error) {
//           console.error('구독 중 오류:', error);
//           isConnected = false;
//           connectionPromise = null;
//           reject(error);
//         }
//       },
      
//       onStompError: (frame) => {
//         console.error('❌ STOMP 오류 발생:');
//         console.error('오류 메시지:', frame.headers['message']);
//         console.error('오류 상세:', frame.body);
//         isConnected = false;
//         connectionPromise = null;
//         reject(new Error(`STOMP 오류: ${frame.headers['message']}`));
//       },
      
//       onWebSocketError: (error) => {
//         console.error('❌ 웹소켓 오류:', error);
//         isConnected = false;
//         connectionPromise = null;
//         reject(error);
//       },
      
//       onDisconnect: () => {
//         console.log('웹소켓 연결이 해제되었습니다.');
//         isConnected = false;
//         connectionPromise = null;
//       },

//       debug: (str) => {
//         console.log('STOMP Debug:', str);
//       }
//     });

//     try {
//       stompClient.activate();
//       console.log('웹소켓 활성화 시도 완료');
//     } catch (error) {
//       console.error('웹소켓 활성화 오류:', error);
//       isConnected = false;
//       connectionPromise = null;
//       reject(error);
//     }
//   });

//   return connectionPromise;
// };

// export const disconnectWebSocket = () => {
//   if (stompClient && stompClient.active) {
//     console.log('웹소켓 연결 해제 중...');
//     stompClient.deactivate();
//   }
//   stompClient = null;
//   isConnected = false;
//   connectionPromise = null;
// };

// export const waitForConnection = (maxWait = 5000) => {
//   return new Promise((resolve, reject) => {
//     const startTime = Date.now();
    
//     const checkConnection = () => {
//       if (isWebSocketConnected()) {
//         resolve(true);
//       } else if (Date.now() - startTime > maxWait) {
//         reject(new Error('웹소켓 연결 대기 시간 초과'));
//       } else {
//         setTimeout(checkConnection, 100);
//       }
//     };
    
//     checkConnection();
//   });
// };