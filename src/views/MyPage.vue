<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="board-menu">
          <div class="left"><h2>마이페이지</h2></div>
          <div class="right">
            <RouterLink to="/mypage/myinformation">회원정보</RouterLink>&nbsp;&nbsp;
            <RouterLink to="/mypage/myreview">나의리뷰</RouterLink>
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
  // import api from '@/plugin/axios'
  // import { onMounted } from 'vue';

  // onMounted(() => {
  // api.get('/mypage')
  //     .then(res => {
  //       console.log(res.data);
  //     })
  //     .catch(err => {
  //       console.error(err);
  //     });
  //   });
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