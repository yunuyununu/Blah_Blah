<template>
  <header class="page-header">
    <div class="container">
      <nav class="navbar navbar-expand-lg">
        <div class="container-fluid">
          <div class="navbar-brand-wrapper">
            <RouterLink to="/" class="navbar-brand" style="font-weight: 700;">
              Blah Blah
            </RouterLink>
            <span class="topic-label">topic</span>
          </div>

          
          <!-- 로그인 상태 확인 후 네비게이션 메뉴 -->
          <ul class="nav justify-content-end" style="gap: 24px;" v-if="isLogin !== null">
            <li class="nav-item">
              <RouterLink to="/">홈</RouterLink>
            </li>
            <li class="nav-item" v-if="isLogin === true">
              <RouterLink to="/mypage/myinformation">마이페이지</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink to="/company/companylist">회사</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink to="/board/boardlist">게시판</RouterLink>
            </li>
            <li class="nav-item" v-if="isLogin === true">
              <!-- 알림 종 모양 아이콘 추가 -->
              <div class="notification-wrapper" @click="toggleNotificationPanel">
                <i class="bell-icon"><svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-bell" viewBox="0 0 16 16">
                  <path d="M8 16a2 2 0 0 0 2-2H6a2 2 0 0 0 2 2M8 1.918l-.797.161A4 4 0 0 0 4 6c0 .628-.134 2.197-.459 3.742-.16.767-.376 1.566-.663 2.258h10.244c-.287-.692-.502-1.49-.663-2.258C12.134 8.197 12 6.628 12 6a4 4 0 0 0-3.203-3.92zM14.22 12c.223.447.481.801.78 1H1c.299-.199.557-.553.78-1C2.68 10.2 3 6.88 3 6c0-2.42 1.72-4.44 4.005-4.901a1 1 0 1 1 1.99 0A5 5 0 0 1 13 6c0 .88.32 4.2 1.22 6"/>
                </svg></i>
                <!-- 읽지 않은 알림이 있다면 빨간 점 표시 -->
                <span v-if="hasUnread" class="red-dot"></span>
              </div>
            </li>
            <li class="nav-item">
              <button v-if="isLogin === true" class="btn btn-outline-secondary" @click="logout">
                로그아웃
              </button>
              <button v-if="isLogin === false" class="btn btn-danger" @click="goLogin">
                로그인
              </button>
            </li>
          </ul>
        </div>
      </nav>
    </div>

    <!-- 알림 목록 패널 (우측 상단에 슬라이딩 패널) -->
    <div v-if="showNotifications" class="notification-panel">
      <div class="notification-header">알림</div>
      <ul class="notification-list">
        <li v-if="notifications.length === 0" style="text-align: center; padding: 20px; color: #888;">
          알림이 없습니다.
        </li>
        <li
          v-for="notification in notifications"
          :key="notification.a_id"
          :class="{ unread: !notification.read }"
        >
        <a
          href="#"
          class="notification-link"
          @click.prevent="goToNotification(notification.a_url)"
        >
          회원님의 글 "{{ notification.b_title }}"에 {{ notification.s_u_name }}님이 좋아요를 눌렀습니다.
        </a></li>
      </ul>
    </div>
  </header>
</template>

<script setup>
import { ref, watch, computed, onMounted,onBeforeUnmount } from 'vue'
import { useUserStore } from '@/store/userStore'
import { useRouter } from 'vue-router'
import axios from 'axios';

const router = useRouter()
const userStore = useUserStore()

const isLogin = computed(() => userStore.isLogin)

// 알림 관련 상태
const notifications = ref([])
const showNotifications = ref(false)
const hasUnread = ref(false)

let socket = null

// 알림 목록 
const fetchNotifications = async () => {
  try {
    const res = await axios.get(`http://localhost:80/alarm/list`) // 로그인된 유저의 알림 목록 요청
    notifications.value = res.data
    console.log("알림리스트==>>>>",notifications.value)
    hasUnread.value = notifications.value.some(n => !n.read)
  } catch (e) {
    console.error('알림 불러오기 실패', e)
  }
}

// // 📌 실시간 알림 수신용 웹소켓 연결
// const connectWebSocket = () => {
//   const userId = userStore.user?.id // 로그인된 사용자 ID (store에 저장된 값 사용)
//   if (!userId) return

//   socket = new WebSocket(`ws://localhost:80/ws/alarm/${userId}`)

//   socket.onmessage = (event) => {
//     const newAlarm = JSON.parse(event.data)
//     notifications.value.unshift(newAlarm)
//     hasUnread.value = true
//   }

//   socket.onclose = () => {
//     console.log('알림 소켓 연결 종료됨')
//     // 자동 재연결 필요시 setTimeout(() => connectWebSocket(), 3000)
//   }
// }

// 📌 알림 패널 열면 읽음 처리 API 호출
const toggleNotificationPanel = async () => {
  showNotifications.value = !showNotifications.value

  if (showNotifications.value) {
    try {
      await axios.post(`http://localhost:80/alarm/isRead`)
      notifications.value = notifications.value.map(n => ({ ...n, read: true }))
      hasUnread.value = false
    } catch (e) {
      console.error('읽음 처리 실패', e)
    }
  }
}

// 페이지가 마운트될 때 알림을 가져옴
onMounted(() => {
  if(userStore.isLogin === true){
    fetchNotifications()
  }
  // 필요하다면 주기적으로 알림을 폴링 할 수 있습니다.
  // setInterval(fetchNotifications, 30000)
})

onBeforeUnmount(() => {
  if (socket) socket.close()
})

const goLogin = () => router.push('/login')
const logout = async () => {
  const confirmLogout = confirm('로그아웃 하시겠습니까?')
      if (confirmLogout) {
          await axios.post('/login/logout')
          await userStore.logout()
          router.push('/login')
      } else {
        return
      }
}

watch(notifications, (newVal) => {
  hasUnread.value = newVal.some(n => !n.read)
}, { deep: true })

const goToNotification = async (url) => {
  showNotifications.value = false

  if (router.currentRoute.value.path === url) {
    // 동일한 경로일 경우 강제로 새로고침
    await router.replace({ path: '/_redirect' }) // 임시 페이지로 이동
    setTimeout(() => {
      router.replace({ path: url })
    }, 10)        // 다시 원래 페이지로 이동
  } else {
    router.push({ path: url })
  }
}
</script>

<style scoped>
.page-header {
  background-color: white;
  color: black;
  padding: 16px;
  text-align: center;
  border-bottom: 1px solid #dee2e6;
  position: relative;
}
.nav {
  color: black;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 24px;
}
.navbar-brand-wrapper {
  position: relative;
  display: inline-block;
}
.topic-label {
  position: absolute;
  top: -6px; 
  right: -7px; 
  font-size: 12px;
  color: red;
  font-weight: bold;
}
/* 알림 아이콘 및 빨간 점 스타일 */
.notification-wrapper {
  position: relative;
  cursor: pointer;
  font-size: 24px;
  margin-right: 16px;
}
.red-dot {
  position: absolute;
  top: 0;
  right: 0;
  width: 10px;
  height: 10px;
  background-color: red;
  border-radius: 50%;
}
/* 알림 패널 스타일 */
.notification-panel {
  position: absolute;
  top: 60px;
  right: 20px;
  width: 300px;
  background: white;
  border: 1px solid #ddd;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

/* 상단 고정 알림 문구 */
.notification-header {
  background-color: #f8f9fa;
  font-weight: bold;
  padding: 12px;
  border-bottom: 1px solid #ddd;
  position: sticky;
  top: 0;
  z-index: 10;
}
.close-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
}
/* 알림 리스트 영역 */
.notification-list {
  max-height: 300px;
  overflow-y: auto;
  margin: 0;
  padding: 0;
  list-style: none;
}

/* 알림 항목 */
.notification-list li {
  padding: 10px;
  border-bottom: 1px solid #eee;
  font-size: 14px;
}
.notification-list li.unread {
  background-color: #f1f3f5;
  font-weight: bold;
}
.notification-link {
  display: block;
  text-decoration: none;
  color: inherit;
  padding: 10px;
}
.notification-link:hover {
  background-color: #e9ecef;
}
</style>
