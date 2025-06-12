<template>
  <footer class="page-footer">
    <div class="footer-links">
      <!-- <router-link to="/about" class="footer-link">회사 소개</router-link> -->
      <router-link to="/conditions" class="footer-link">이용약관</router-link>
      <router-link to="/privacy" class="footer-link">개인정보처리방침</router-link>
    </div>
    <div class="footer-cs">
      고객센터: jyw990802@gmail.com | 운영시간: 평일 10:00 ~ 18:00 (점심시간 12:00 ~ 13:00)
    </div>
    <div class="footer-copy">
      © 2025 익명커뮤니티 Blah Blah. All rights reserved.
    </div>
  </footer>
</template>

<script setup>
// 특별한 로직은 필요 없음
</script>

<style scoped>
.page-footer {
  background-color: #f8f9fa;
  color: #555;
  font-size: 14px;
  padding: 24px 16px;
  border-top: 1px solid #ddd;
  text-align: center;
  line-height: 1.6;
}

.footer-links {
  margin-bottom: 8px;
}

.footer-link {
  color: #666;
  margin: 0 10px;
  text-decoration: none;
}

.footer-link:hover {
  text-decoration: underline;
  color: #000;
}

.footer-cs {
  font-size: 13px;
  color: #666;
  margin-bottom: 6px;
}

.footer-copy {
  font-size: 12px;
  color: #aaa;
}
</style>
