<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="board-menu">
          <div class="left">
            <h2>회사</h2>
          </div>
          <div class="right">
            <RouterLink 
              to="/company/companylist"
              class="menu-tab"
              :class="{ active: route.path.includes('/company/companylist') }"
            >
              회사 목록
            </RouterLink>
            <RouterLink 
              to="/company/companyInsert"
              v-if="userStore.isLogin"
              class="menu-tab"
              :class="{ active: route.path.includes('/company/companyInsert') }"
            >
              회사 신청
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
import { useUserStore } from '@/store/userStore'
import { useRoute } from 'vue-router'

const userStore = useUserStore()
const route = useRoute()
</script>

<style scoped>
.container {
  padding: 16px;
}

.board-menu {
  display: flex;
  flex-wrap: wrap;
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
  flex-wrap: wrap;
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
