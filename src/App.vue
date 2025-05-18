<script setup>
import PageHeader from './components/PageHeader.vue';
import PageFooter from './components/PageFooter.vue';
import 'bootstrap/dist/css/bootstrap.min.css';
</script>

<template>
  <div class="layout">
    <PageHeader/>
    <br>
    <div class="content">
      <div class="container">
        <router-view></router-view>
      </div>
    </div>
    <br>
    <PageFooter/>
  </div>
</template>

<style>
/* layout 전체가 100vh를 차지 */
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
/* content 영역이 자동으로 남은 공간 차지 */
.content {
  flex: 1;
}
a.router-link-active,
a.router-link-exact-active,
a {
  color: black;
  text-decoration: none;
}

a:hover {
  text-decoration: none;
}
</style>
