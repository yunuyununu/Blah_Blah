<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col-4">
          <h2>회사</h2>
          <RouterLink to="/company/companylist">회사 목록</RouterLink>
          <br>
          <RouterLink to="/company/companychange">회사 신청</RouterLink>
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