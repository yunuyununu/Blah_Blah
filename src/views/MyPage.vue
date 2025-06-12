<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="board-menu">
          <div class="left"><h2>마이페이지</h2></div>
          <div class="right">
            <RouterLink 
              to="/mypage/myinformation"
              class="menu-tab"
              :class="{ active: $route.path.includes('/mypage/myinformation') }"
            >
              회원 정보
            </RouterLink>
            <RouterLink 
              to="/mypage/myreview"
              class="menu-tab"
              :class="{ active: $route.path.includes('/mypage/myreview') }"
            >
              나의리뷰
            </RouterLink>
            <RouterLink 
              to="/mypage/mycompanychange"
              class="menu-tab"
              :class="{ active: $route.path.includes('/mypage/mycompanychange') }"
            >
              회사 변경 신청
            </RouterLink>
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
  align-items: flex-end;
  border-bottom: 2px solid #e5e7eb;
  padding-bottom: 0;
  margin-bottom: 16px;
}

.board-menu .left {
  font-size: 20px;
  font-weight: 600;
  padding-bottom: 12px;
}

.board-menu .right {
  display: flex;
  gap: 4px;
}

.menu-tab {
  position: relative;
  padding: 12px 20px;
  text-decoration: none;
  color: #6b7280;
  background-color: #f9fafb;
  border: 1px solid #e5e7eb;
  border-bottom: none;
  border-radius: 8px 8px 0 0;
  font-weight: 500;
  transition: all 0.2s ease;
  margin-bottom: -2px;
}

.menu-tab:hover {
  color: #374151;
  background-color: #f3f4f6;
}

.menu-tab.active {
  color: #3b82f6;
  background-color: #ffffff;
  border-color: #3b82f6;
  z-index: 1;
}

.menu-tab.active::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  right: 0;
  height: 2px;
  background-color: #ffffff;
  z-index: 2;
}
  </style>