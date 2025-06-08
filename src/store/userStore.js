import { defineStore } from 'pinia'
import axios from 'axios'

axios.defaults.withCredentials = true;

export const useUserStore = defineStore('user', {
  state: () => ({
    isLogin: null,
    userIdx: null,
    notifications: [],
    hasUnread: false,
    sse: null, // SSE를 state에서 관리
  }),
  persist: {
    enabled: true,
    storage: localStorage,
    paths: ['isLogin', 'userIdx', 'notifications', 'hasUnread'] // SSE는 persist하지 않음
  },
  actions: {
    async checkSession() {
      try {
        const res = await axios.post('/login/checkSession', {}, { withCredentials: true })
        this.isLogin = res.data.isLogin
        this.userIdx = res.data.userIdx ?? null
        console.log('로그인상태===>>>', this.isLogin, 'UserIdx:', this.userIdx)

        // 로그인 상태 유지 시에도 SSE 재연결
        if (this.isLogin && this.userIdx) {
          this.subscribeToAlarm()
        }
      } catch (e) {
        this.isLogin = false
        this.userIdx = null
      }
    },

    async logout() {
      this.isLogin = false
      this.userIdx = null
      this.notifications = [] // 알림 데이터 정리
      this.hasUnread = false
      
      // SSE 연결 정리
      this.cleanupSSE()
    },

    async setLoginSuccess(userIdx) {
      this.isLogin = true
      this.userIdx = userIdx
      console.log('로그인성공상태==>', this.isLogin, 'UserIdx:', this.userIdx)

      // 로그인 성공 시 SSE 연결
      this.subscribeToAlarm()
    },


    

    async fetchNotifications() {
      try {
        const res = await axios.get(`/alarm/list`)
        this.notifications = res.data
        this.hasUnread = this.notifications.some(n => !n.read)
      } catch (e) {
        console.error('알림 불러오기 실패', e)
      }
    },

    async markAllAsRead() {
      try {
        await axios.post(`/alarm/isRead`)
        this.notifications = this.notifications.map(n => ({ ...n, read: true }))
        this.hasUnread = false
      } catch (e) {
        console.error('읽음 처리 실패', e)
      }
    },


    subscribeToAlarm() {
      if (!this.userIdx) {
        console.log('userIdx가 없어서 SSE 연결 불가')
        return;
      }

      this.cleanupSSE()

      console.log(`SSE 연결 시도: /alarm/subscribe/${this.userIdx}`)
      
      try {
        this.sse = new EventSource(`/alarm/subscribe/${this.userIdx}`)

        this.sse.onopen = (event) => {
          console.log("SSE 연결 성공", event)
        }

        // 특정 타입별 이벤트 리스너들
        this.sse.addEventListener("like", (event) => {
          console.log("좋아요 알림 수신:", event.data)
          try {
            const alarmData = JSON.parse(event.data)
            this.addNotification(alarmData)
            this.notifications.unshift(alarmData);
            this.hasUnread = true;
          } catch (e) {
            console.error("좋아요 알림 파싱 실패:", e)
          }
        })

        // 다른 타입의 알림도 추가할 수 있습니다
        // this.sse.addEventListener("comment", (event) => {
        //   console.log("댓글 알림 수신:", event.data)
        //   try {
        //     const alarmData = JSON.parse(event.data)
        //     this.addNotification(alarmData)
        //   } catch (e) {
        //     console.error("댓글 알림 파싱 실패:", e)
        //   }
        // })


            // 기본 메시지 핸들러 (위에서 처리되지 않은 모든 메시지)
          this.sse.onmessage = (event) => {
            console.log("기본 메시지 수신:", event.data)
            // 이미 특정 이벤트로 처리되지 않은 경우에만 처리
            if (event.type === 'message') {
              try {
                const alarmData = JSON.parse(event.data)
                this.addNotification(alarmData)
              } catch (e) {
                console.error("기본 메시지 파싱 실패:", e)
              }
            }
          }

        this.sse.onerror = (error) => {
          console.error("SSE 오류:", error)
          console.error("SSE readyState:", this.sse?.readyState)
          
          // 연결 정리
          this.cleanupSSE()
          
          // 재연결 시도 (로그인 상태일 때만)
          if (this.isLogin && this.userIdx) {
            setTimeout(() => {
              console.log("SSE 재연결 시도")
              this.subscribeToAlarm()
            }, 3000) // 3초로 단축
          }
        }

      } catch (error) {
        console.error("SSE 연결 생성 실패:", error)
      }
    },

    // 알림 추가 헬퍼 메소드
    addNotification(alarmData) {
      console.log("새 알림 추가:", alarmData)
      
      // 기존 알림 목록에 새 알림 추가
      this.notifications.unshift({
        message: alarmData.message,
        read: false,
        timestamp: alarmData.timestamp,
        type: alarmData.type
      })
      
      this.hasUnread = true
      
      // 브라우저 알림도 표시 (권한이 있는 경우)
      if (Notification.permission === 'granted') {
        new Notification('새 알림', {
          body: alarmData.message,
          icon: '/favicon.ico'
        })
      }
    },

    // 브라우저 알림 권한 요청
    async requestNotificationPermission() {
      if ('Notification' in window) {
        const permission = await Notification.requestPermission()
        console.log('알림 권한:', permission)
        return permission === 'granted'
      }
      return false
    },
    // SSE 연결을 안전하게 정리하는 헬퍼 메서드
    cleanupSSE() {
      if (this.sse) {
        console.log('기존 SSE 연결 정리')
        try {
          // EventSource 객체인지 확인하고 close 메서드가 있는지 체크
          if (this.sse instanceof EventSource && typeof this.sse.close === 'function') {
            this.sse.close()
            console.log('SSE 연결 정상 종료')
          } else {
            console.warn('SSE 객체가 올바르지 않음:', this.sse)
          }
        } catch (e) {
          console.error('SSE 종료 중 오류:', e)
        }
        this.sse = null
      }
    }

    
  }
})