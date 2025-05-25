import { defineStore } from 'pinia'
import axios from 'axios'

axios.defaults.withCredentials = true;

export const useUserStore = defineStore('user', {
  state: () => ({
    isLogin: null, // null: 확인 중, true: 로그인, false: 로그아웃
  }),
  persist: {
    enabled: true, // persistedState 활성화
    storage: localStorage // 저장 방식 (localStorage, sessionStorage)
  },
  actions: {
    async checkSession() {
      try {
        const res = await axios.post('/login/checkSession', { withCredentials: true })
        this.isLogin = res.data === true
         console.log('로그인상태===', this.isLogin)
      } catch (e) {
        this.isLogin = false
         console.error('로그아웃상태====',e)
      }
    },
    async logout() {

      const confirmLogout = confirm('로그아웃 하시겠습니까?')
      if (!confirmLogout) return

      try {
        await axios.post('/login/logout')
        this.isLogin = false
      } catch (e) {
        console.error('로그아웃 실패', e)
      }
    },
    async setLoginSuccess() {
      this.isLogin = true
      console.log('로그인상태', this.isLogin)
    }
  }
})
