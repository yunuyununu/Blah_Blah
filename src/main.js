import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPersistedState from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from './router'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'

// Pinia 스토어 import
import { useUserStore } from './store/userStore'

const vuetify = createVuetify({
  components,
  directives,
})

// 앱 생성
const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPersistedState)

// Pinia 먼저 등록 (스토어 사용 가능하도록)
app.use(pinia)

// 세션 확인 후 mount
const userStore = useUserStore()

// 세션 체크 후 앱 마운트
userStore.checkSession().finally(() => {
  app.use(vuetify).use(router).mount('#app')
})
