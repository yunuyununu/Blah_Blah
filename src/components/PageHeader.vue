<template>
  <header class="page-header">
    <div class="container">
      <nav class="navbar navbar-expand-lg">
        <div class="container-fluid">
          <div class="navbar-brand-wrapper">
            <a class="navbar-brand" style="font-weight: 700;">Blah Blah</a>
            <span class="topic-label">topic</span>
          </div>

          <!-- 로그인 상태 확인되면 보여줌 -->
          <ul
            class="nav justify-content-end"
            style="gap: 24px;"
            v-if="userStore.isLogin !== null"
          >
            <li class="nav-item">
              <RouterLink to="/">홈</RouterLink>
            </li>
            <li class="nav-item" v-if="userStore.isLogin === true">
              <RouterLink to="/mypage/myinformation">마이페이지</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink to="/company/companylist">회사</RouterLink>
            </li>
            <li class="nav-item">
              <RouterLink to="/board/boardlist">게시판</RouterLink>
            </li>
            <li class="nav-item">
              <button
                v-if="userStore.isLogin === true"
                class="btn btn-outline-secondary"
                @click="logout"
              >
                로그아웃
              </button>
              <button
                v-if="userStore.isLogin === false"
                class="btn btn-danger"
                @click="goLogin"
              >
                로그인
              </button>
            </li>
          </ul>
        </div>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { useUserStore } from '@/store/userStore'
import { useRouter } from 'vue-router'
import { onMounted } from 'vue'

const router = useRouter(); //기능
const userStore = useUserStore()

const goLogin = () => router.push('/login')

const logout = async () => {
  await userStore.logout()
  router.push('/login')
}

onMounted(() => {
  userStore.checkSession()
})
</script>

<style scoped>
.page-header {
  background-color: white;
  color: Black;
  padding: 16px;
  text-align: center;
  border-bottom: 1px solid #dee2e6;
}
nav {
  color: Black;
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
</style>