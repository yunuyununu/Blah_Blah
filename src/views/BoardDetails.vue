<template>
  <div class="post-container" v-if="post">
    <div class="post-header">
      <h2>{{ post.B_TITLE }}</h2>
      <div class="post-meta">
        <span>{{ post.C_NAME }} · {{ post.U_NICNAME }}</span>
        <span>👁 {{ post.B_HITS }} · ♥ ♡ {{ post.B_LIKES || 0 }} · {{ formatDate(post.B_DATE) }}</span>
      </div>
    </div>
    <div class="post-content">
      <p v-html="post.B_CONTENT"></p>
    </div>
    <button class="back-button" @click="$router.back()">← 목록으로</button>
  </div>
  <div v-else class="loading">불러오는 중...</div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const post = ref(null);

const fetchPostDetail = async () => {
  try {
    const res = await axios.get(`http://localhost:80/board/details`, {
      params: { b_idx: route.params.b_idx },
    });
    post.value = res.data;
  } catch (err) {
    console.error('게시글 로딩 실패:', err);
  }
};

const formatDate = (datetime) => {
  const date = new Date(datetime);
  return date.toLocaleString();
};

onMounted(() => {
  fetchPostDetail();
});
</script>

<style scoped>
.post-container {
  max-width: 800px;
  margin: auto;
  padding: 24px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.post-header h2 {
  font-size: 24px;
  margin-bottom: 8px;
}

.post-meta {
  font-size: 14px;
  color: #888;
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

.post-content {
  font-size: 16px;
  line-height: 1.6;
  color: #333;
  white-space: pre-wrap;
}

.back-button {
  margin-top: 24px;
  padding: 10px 16px;
  background: #f44336;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}

.loading {
  text-align: center;
  padding: 40px;
  font-size: 18px;
  color: #888;
}
</style>
