<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col-4">
          <h2>게시판</h2>
          <RouterLink to="/board/boardlist">게시글 목록</RouterLink>
          <br>
          <RouterLink to="/board/boardregistration">게시글 등록</RouterLink>
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