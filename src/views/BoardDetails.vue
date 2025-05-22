<template>
  <div class="post-container" v-if="post">
    <!-- 게시글 내용 -->
    <div class="post-header">
      <h2>{{ post.B_TITLE }}</h2>
      <br>
      <div class="post-meta">
        <span>{{ post.C_NAME }} · {{ post.U_NICNAME }}</span>
        <span>👁 {{ post.B_HITS }} · ♥ ♡ {{ post.B_LIKES || 0 }} · {{ formatDate(post.B_DATE) }}</span>
      </div>
    </div>
    <hr />
    <div class="post-content">
      <p v-html="post.B_CONTENT"></p>
    </div>
    <button class="btn btn-outline-danger" @click="$router.back()"  style="text-align: left;">← 목록으로</button>
    <hr />

       <div class="comment-section">
      <h3>댓글 {{ comments.length }}</h3>
      <textarea v-model="newComment" placeholder="댓글을 남겨주세요."></textarea>
      <br />
      <div class="comment-action">
        <button class="btn btn-outline-primary" @click="submitComment">등록</button>
      </div>

      <div class="comment-list" v-if="comments.length > 0">
        <div v-for="comment in comments" :key="comment.id" class="comment-item">
          <div class="comment-header">
            <strong>{{ comment.nickname }}</strong>
            <span class="comment-date">{{ formatDate(comment.date) }}</span>
          </div>
          <div class="comment-body">{{ comment.content }}</div>
        </div>
      </div>

      <div v-else>
      </div>
    </div>
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

// comment
const comments = ref([
  // 예시 데이터 (백엔드 연동 전)
  // { id: 1, nickname: '익명1', content: '좋은 글 감사합니다.', date: new Date() },
  // { id: 2, nickname: '익명2', content: '공감합니다!', date: new Date() }
]);

const newComment = ref('');

const submitComment = () => {
  if (!newComment.value.trim()) return;
  comments.value.push({
    id: Date.now(),
    nickname: '익명',
    content: newComment.value,
    date: new Date()
  });
  newComment.value = '';
};
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

hr {
  color:#888;
}
/* comment */
.comment-section {
  margin-top: 32px;
}

.comment-section h3 {
  font-size: 18px;
  margin-bottom: 12px;
}

.comment-section textarea {
  width: 100%;
  min-height: 80px;
  padding: 12px;
  font-size: 14px;
  resize: vertical;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.submit-button {
  margin-top: 8px;
  padding: 8px 16px;
  background-color: #1976d2;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}

.comment-list {
  margin-top: 24px;
}

.comment-item {
  border-top: 1px solid #eee;
  padding: 12px 0;
}

.comment-header {
  font-size: 14px;
  color: #555;
  display: flex;
  justify-content: space-between;
}

.comment-body {
  font-size: 15px;
  color: #333;
  margin-top: 4px;
}
.comment-action {
  margin-top: 8px;
  display: flex;
  justify-content: flex-end;
}
</style>
