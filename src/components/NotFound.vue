<template>
  <div class="not-found-simple">
    <h2>앗, 페이지를 찾을 수 없어요!</h2>
    <p>잠시 후에 자동으로 홈으로 이동합니다.</p>
    <p>잠시만 기다려주세요…</p>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { onMounted } from 'vue';

const router = useRouter();

onMounted(() => {
  // 3초 뒤에 홈으로 자동 이동
  setTimeout(() => {
    router.push('/');
  }, 3000);
});
</script>

<style scoped>
.not-found-simple {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 60vh;
  color: #666;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  text-align: center;
}

h2 {
  font-weight: 600;
  margin-bottom: 10px;
  color: #333;
}

p {
  margin: 5px 0;
  font-size: 1rem;
}
</style>
