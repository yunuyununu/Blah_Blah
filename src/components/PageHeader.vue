<!-- PageHeader.vue -->
<template>
  <header class="page-header">
    <div class="container">
      <nav class="navbar navbar-expand-lg">
        <div class="container-fluid">
          <div class="navbar-brand-wrapper">
            <a class="navbar-brand" href="/" style="font-weight: 700;">Blah Blah</a>
            <span class="topic-label">topic</span>
          </div>

          <ul class="nav justify-content-end" style="gap: 24px;">
          <li class="nav-item">
            <RouterLink to="/">홈</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/mypage/myinformation">마이페이지</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/company/companylist">회사</RouterLink>
          </li>
          <li class="nav-item">
            <RouterLink to="/board/boardlist">게시판</RouterLink>
          </li>
          <li class="nav-item">
            <button class="btn btn-danger" @click="goJoin">회원가입</button>
          </li>
        </ul>
        </div>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router';
const router = useRouter(); //기능
const route = useRoute(); //정보
console.log('route.name: ', route.name); // home
console.log('route.path: ', route.path); // /

const goJoin = () => router.push('/join');
</script>

<style scoped>
.page-header {
  background-color: white;
  color: Black;
  padding: 16px;
  text-align: center;
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