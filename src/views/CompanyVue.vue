<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="board-menu">
          <div class="left"><h2>회사</h2></div>
          <div class="right">
            <RouterLink to="/company/companylist">회사 목록</RouterLink>&nbsp;&nbsp;
            <RouterLink to="/company/companyInsert">회사 신청</RouterLink>
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