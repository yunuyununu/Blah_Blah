import { defineStore } from 'pinia'
import axios from 'axios'

export const useUserStore = defineStore('user', {
  state: () => ({
    isLogin: null, // null: 확인 중, true: 로그인, false: 로그아웃
  }),
  actions: {
    async checkSession() {
      try {
        const res = await axios.get('/login/checkSession')
        this.isLogin = res.data === true
      } catch (e) {
        this.isLogin = false
      }
    },
    async logout() {
      try {
        await axios.post('/login/logout')
        this.isLogin = false
      } catch (e) {
        console.error('로그아웃 실패', e)
      }
    },
    setLoginSuccess() {
      this.isLogin = true
    }
  },
})
