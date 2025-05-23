<script setup>
import PageHeader from './components/PageHeader.vue';
import PageFooter from './components/PageFooter.vue';
import 'bootstrap/dist/css/bootstrap.min.css';

import { useUserStore } from '@/store/userStore'
import { onMounted } from 'vue'

const userStore = useUserStore()

onMounted(() => {
  userStore.checkSession()
})
</script>

<template>
  <div class="layout">
    <PageHeader class="fixed-header"/>
    <div class="content">
      <div class="container">
        <router-view></router-view>
      </div>
    </div>
    <PageFooter/>
  </div>
</template>

<style>
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 헤더 고정 */
.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000; /* 필요에 따라 조절 */
}

/* 헤더 높이 만큼 컨텐츠 위쪽 여백 추가 */
/* 예: 헤더 높이가 60px이라면 */
.content {
  flex: 1;
  margin-top: 60px; /* 헤더 높이와 맞추세요 */
}

a.router-link-active,
a.router-link-exact-active,
a {
  color: black;
  text-decoration: none;
}

a:hover {
  text-decoration: none;
}
</style>
