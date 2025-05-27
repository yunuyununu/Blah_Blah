<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="board-menu">
          <div class="left"><h2>게시판</h2></div>
          <div class="right">
            <RouterLink to="/board/boardlist" class="text-blue-600 hover:underline">게시글 목록</RouterLink>&nbsp;&nbsp;
            <RouterLink to="/board/boardregistration" 
            class="text-blue-600 hover:underline" 
            v-if="userStore.isLogin == true">게시글 등록</RouterLink>
          </div>
        </div>
      </div>
      <div class="row">
        <router-view></router-view>
      </div>
    </div>
  </div>
  </template>
  
  <script setup>
  import { useUserStore } from '@/store/userStore'

const userStore = useUserStore()
  </script>
  
  <style scoped>
.container {
  padding: 16px;
}

.header {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 16px;
}

.board-menu {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ccc;
  padding-bottom: 8px;
  margin-bottom: 16px;
}

.board-menu .left {
  font-size: 20px;
  font-weight: 600;
}

.board-menu .right button {
  margin-left: 8px;
  padding: 6px 12px;
  border: none;
  background-color: #eee;
  cursor: pointer;
  border-radius: 4px;
}

.board-menu .right button:last-child {
  background-color: #007bff;
  color: white;
}

  </style>