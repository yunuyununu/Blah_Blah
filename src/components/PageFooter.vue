<template>
  <footer class="page-footer">
    <router-link to="/about" class="footer-link">서비스 소개</router-link> |
    <router-link to="/conditions" class="footer-link">이용약관</router-link> |
    <router-link to="/directory" class="footer-link">디렉토리</router-link> |
    <router-link to="/privacy" class="footer-link">개인정보 처리방침</router-link>
  </footer>
</template>

<script setup>
// 특별한 로직은 필요 없음
</script>

<style scoped>
.page-footer {
  height: 60px;
  background-color: #c2c2c2;
  color: rgb(0, 0, 0);
  padding: 16px;
  text-align: center;
}

.footer-link {
  color: black;
  text-decoration: none;
  margin: 0 8px;
}

.footer-link:hover {
  text-decoration: underline;
}
</style>