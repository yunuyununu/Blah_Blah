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
        <h3>댓글 {{ commentList.length }}</h3>
        <textarea v-model="newComment" placeholder="댓글을 남겨주세요."></textarea>
        <div class="comment-action">
          <button class="btn btn-outline-primary" @click="submitComment">등록</button>
        </div>

        <div class="comment-list" v-if="commentList.length > 0">
          <div v-for="comment in commentList" :key="comment.cm_idx" class="comment-item">
            <div class="comment-header">
              <!-- <span class="nickname">{{ maskedNickname(comment.u_nicname || '익명') }}</span> -->
               <span class="nickname">
                <span class="nickname-blue">{{ comment.c_nicname }}</span> · <span class="nickname-gray">{{ comment.u_nicname }}</span>
              </span>
              <span class="comment-date">{{ formatDate(comment.cm_date) }}</span>
            </div>
            <div class="comment-body">{{ comment.cm_content }}</div>
            <div class="comment-footer">
              <!-- <span class="action">👍 좋아요</span> -->
              <span class="action">💬 답글</span>
            </div>
          </div>
        </div>

        <div v-else>
          <p>작성된 댓글이 없습니다.</p>
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

const bidx = route.params.b_idx;


const fetchPostDetail = async () => {
  try {
    const res = await axios.get(`http://localhost:80/board/details`, {
      params: { b_idx: bidx },
    });
    post.value = res.data;
  } catch (err) {
    console.error('게시글 로딩 실패:', err);
  }
};

const commentList = ref([]);
const fetchPostComment = async () => {
  try {
    const res = await axios.get(`http://localhost:80/reply/list`, {
      params: { cmBIdx: bidx }
    });
    commentList.value = res.data;
    console.log("댓글목록==>", commentList.value)
  } catch (err) {
    console.error('댓글 로딩 실패:', err);
  }
};



const formatDate = (datetime) => {
  const date = new Date(datetime);
  return date.toLocaleString();
};

onMounted(() => {
  fetchPostDetail();
  fetchPostComment();
});


const newComment = ref('');

const submitComment = () => {
  if (!newComment.value.trim()) return;

  commentList.value.push({
    cm_idx: Date.now(),
    u_nicname: '익명', // 실제 로그인 사용자 정보 연동 필요
    cm_content: newComment.value,
    cm_date: new Date().toISOString()
  });

  newComment.value = '';
};


// 미인증 회원일때 닉네임 마스킹 함수
// const maskedNickname = (nickname) => {
//   if (!nickname) return '익명';
//   return nickname.slice(0, 1) + '*'.repeat(Math.max(1, nickname.length - 1));
// };

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
  padding: 16px 0;
  display: flex;
  flex-direction: column;
  gap: 6px;
}


.comment-header {
  font-size: 13px;
  color: #555;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.comment-body {
  font-size: 15px;
  color: #222;
  margin-left: 4px;
  white-space: pre-line;
}

.comment-footer {
  font-size: 13px;
  color: #888;
  display: flex;
  gap: 16px;
  margin-top: 4px;
}

.comment-footer .action {
  cursor: pointer;
  transition: color 0.2s;
}

.comment-footer .action:hover {
  color: #1976d2;
}

.comment-action {
  margin-top: 8px;
  display: flex;
  justify-content: flex-end;
}

.nickname {
  font-weight: bold;
  color: #333;
}

.nickname-blue {
  color: #66a4e2; /* 파란색 */
  font-weight: bold;
}

.nickname-gray {
  color: #919191; /* 파란색 */
  font-weight: bold;
}
</style>
