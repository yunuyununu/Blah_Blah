<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col-4">
          <h2>마이페이지</h2>
          <RouterLink to="/mypage/myinformation">회원정보</RouterLink>
          <br>
          <RouterLink to="/mypage/myreview">나의리뷰</RouterLink>
        </div>
        <div class="col-8">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script setup>
  
  </script>
  
  <style scoped>

  </style>