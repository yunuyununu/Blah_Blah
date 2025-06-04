import { defineStore } from 'pinia'
import axios from 'axios'

axios.defaults.withCredentials = true;

export const useUserStore = defineStore('user', {
  state: () => ({
    isLogin: null, // null: 확인 중, true: 로그인, false: 로그아웃
    userIdx: null,
  }),
  persist: {
    enabled: true, // persistedState 활성화
    storage: localStorage // 저장 방식 (localStorage, sessionStorage)
  },
  actions: {
    async checkSession() {
      try {
        const res = await axios.post('/login/checkSession', {},{ withCredentials: true })
        this.isLogin = res.data.isLogin
        this.userIdx = res.data.userIdx ?? null
        console.log('로그인상태===>>>', this.isLogin, 'UserIdx:', this.userIdx)
      } catch (e) {
        this.isLogin = false
        this.userIdx = null
        console.error('로그아웃상태====', e)
      }
    },
    async logout() {
          this.isLogin = false
          this.userIdx = null
    },
    async setLoginSuccess(userIdx) {
      this.isLogin = true
      this.userIdx = userIdx
      console.log('로그인성공상태==>', this.isLogin, 'UserIdx:', this.userIdx)
    }
  }
})
