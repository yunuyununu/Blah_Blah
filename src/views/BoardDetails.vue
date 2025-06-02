<template>
  <div class="post-container" v-if="post">
    <!-- 게시글 내용 -->
    <div class="post-header">
      <h2>{{ post.B_TITLE }}</h2>
      <br>
      <div class="post-meta">
        <span>{{ post.C_NAME }} · {{ post.U_NICNAME }}</span>
        <div class="post-meta-right">
          <span><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-eye" viewBox="0 0 16 16">
          <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8M1.173 8a13 13 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5s3.879 1.168 5.168 2.457A13 13 0 0 1 14.828 8q-.086.13-.195.288c-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5s-3.879-1.168-5.168-2.457A13 13 0 0 1 1.172 8z"/>
          <path d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5M4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0"/>
        </svg>&nbsp; {{ post.B_HITS }}</span>
          <button @click="toggleLike" class="like-button">
            <span class="heart-icon" :class="{ liked: isLiked }">
            {{ isLiked ? '♥' : '♡' }} {{ likeCount || 0 }}
            </span>
          </button>
          <span>{{ formatDate(post.B_DATE) }}</span>
        </div>
      </div>

    </div>
    <hr />
    <div class="post-content">
      <p v-html="post.B_CONTENT"></p>
    </div>
    <div class="board-images" v-if="boardImages.length > 0">
      <img
        v-for="img in boardImages"
        :key="img.I_IDX"
        :src="getImageUrl(img.I_IMAGE)"
        alt="게시글 이미지"
        class="board-image"
      />
    </div>
    <br>
    <button class="btn btn-outline-danger" @click="$router.back()"  style="text-align: left;">← 목록으로</button>
        <div v-if="userStore.userIdx === post.B_U_IDX" style="text-align:right">
          <span class="action" @click="updateBoard">✏️ 수정</span>
          <span class="action" @click="deleteBoard">🗑️ 삭제</span>
        </div>
    <hr />

      <div class="comment-section">
        <h3>댓글 {{ commentList.length }}</h3>
        <textarea v-model="newComment" placeholder="댓글을 남겨주세요."></textarea>
        <div class="comment-action">
          <button class="btn btn-outline-primary" @click="submitComment">등록</button>
        </div>
         <div class="comment-list" v-if="threadedComments.length > 0">
          <CommentItem 
            v-for="comment in threadedComments"
            :key="comment.cm_idx"
            :comment="comment"
            :reply-to-list="replyToList"
            :reply-content-map="replyContentMap"
            @toggle-reply="toggleReply"
            @submit-reply="submitReply"
            @update-reply-content="updateReplyContent"
            @delete-comment="handleDeleteComment"
          />
        </div>

        <div v-else>
          <p>작성된 댓글이 없습니다.</p>
        </div>
      </div>
  </div>
  <div v-else class="loading">불러오는 중...</div>
</template>

<script setup>
import CommentItem from '@/views/CommentItem.vue';
import { useUserStore } from '@/store/userStore'
import { useAlarmStore } from '@/store/alarmStore'
import { useRouter } from 'vue-router'


const alarmStore = useAlarmStore()
const userStore = useUserStore()
const router = useRouter()

import { onMounted, ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const post = ref(null);
const bidx = route.params.b_idx;
const buidx = ref(null);
const btitle = ref(null);

// 게시글 이미지
const boardImages = ref([]);

const commentList = ref([]);
const newComment = ref('');
// const replyTo = ref(null);
const replyToList = ref(new Set());
const replyContentMap = ref({});

// 답글 토글: 같은 댓글이면 닫고, 아니면 열기
const toggleReply = (cm_idx) => {
  if (replyToList.value.has(cm_idx)) {
    replyToList.value.delete(cm_idx);
  } else {
    replyToList.value.add(cm_idx);
}
  // 반응성 위해 새 Set 할당
  replyToList.value = new Set(replyToList.value);
};

// 답글 입력 내용 업데이트
const updateReplyContent = (cm_idx, value) => {
  replyContentMap.value = { ...replyContentMap.value, [cm_idx]: value };
};

// 댓글 삭제
const handleDeleteComment = async (cm_idx) => {
  try {
    const confirmed = window.confirm('정말 이 댓글을 삭제하시겠습니까?');
    if (!confirmed) return;

    await axios.post(`http://localhost:80/reply/commentDelete`, {
      cm_idx : cm_idx
    });
    alert('댓글이 삭제되었습니다.');

    // 댓글 목록 갱신
    fetchPostComment(); // 댓글 목록 다시 불러오는 함수
  } catch (err) {
    alert('댓글 삭제 중 오류가 발생했습니다.');
    console.error(err);
  }
};

const fetchPostDetail = async () => {
  try {
    const res = await axios.get(`http://localhost:80/board/details`, {
      params: { b_idx: bidx },
    });
    post.value = res.data;
    buidx.value = res.data.B_U_IDX;
    btitle.value = res.data.B_TITLE;
    // 이미지 리스트 요청
    const imageRes = await axios.get(`http://localhost:80/board/boardImages`, {
      params: { b_idx: bidx },
    });
    boardImages.value = imageRes.data.filter(img => img !== null);
  } catch (err) {
    console.error('게시글 로딩 실패:', err);
  }
};

const getImageUrl = (filename) => {
  if (filename.startsWith('https')) {
    return filename;
  } else if (filename !== "") {
    return `https://storage.googleapis.com/blah_blah_bucket/${filename}`;
  } else {
    return `https://storage.googleapis.com/blah_blah_bucket/no_image.png`;
  }
};

const fetchPostComment = async () => {
  try {
    const res = await axios.get(`http://localhost:80/reply/list`, {
      params: { cmBIdx: bidx }
    });
    commentList.value = res.data;
  } catch (err) {
    console.error('댓글 로딩 실패:', err);
  }
};

const updateBoard = async () => {
  try {

  } catch(err) {
    
  }
}

const deleteBoard = async () => {
  try {
    const confirmed = window.confirm('정말 이 게시물을 삭제하시겠습니까?');
    if (!confirmed) return;
    await axios.post(`http://localhost:80/board/boardDelete`, {
      b_idx: bidx
    });
    router.push('/board/boardlist')
  } catch (err) {
    console.error('게시글 삭제 실패:', err);
  }
};

onMounted(async () => {
  fetchPostDetail();
  fetchPostComment();
  
  if (userStore.isLogin) {
    fetchLikeStatus();
    
    // 웹소켓 연결 상태 확인 (단순화)
    if (!alarmStore.connected) {
      console.log(' 웹소켓 연결 상태:', {
        connected: alarmStore.connected,
        connecting: alarmStore.connecting
      });
    }
  } else {
    fetchLikeCount();
  }
});

const formatDate = (datetime) => {
  const date = new Date(datetime);
  return date.toLocaleString();
};

const threadedComments = computed(() => {
  const map = {};
  const roots = [];
  commentList.value.forEach(comment => {
    comment.replies = [];
    map[comment.cm_idx] = comment;
  });
  commentList.value.forEach(comment => {
    if (comment.cm_parent_idx) {
      const parent = map[comment.cm_parent_idx];
      if (parent) parent.replies.push(comment);
    } else {
      roots.push(comment);
    }
  });
  return roots;
});

const submitComment = async() => {
  if(userStore.isLogin === false){
    alert('로그인 후 이용가능합니다.')
    return
  }
  if (!newComment.value.trim()) {
    alert('댓글을 작성해주세요.')
    return;
  }
  if(userStore.isLogin !== false){
    try {
      const response = await axios.post('http://localhost:80/reply/commentInsert', {
        cm_b_idx: bidx,
        cm_content: newComment.value,
      })
      if(response.data === "success"){
        newComment.value = '';
        fetchPostComment();
      } else {
        alert('댓글 등록에 실패했습니다.')
      }
    } catch (err) {
      console.error('댓글 등록 실패:', err);
    }
  }else{
    alert('로그인 후 이용해주세요.')
    return
  }
};

// 답글 등록
const submitReply = async (parentIdx) => {
  if(userStore.isLogin === false){
    alert('로그인 후 이용가능합니다.')
    return
  }
  const content = replyContentMap.value[parentIdx];
  if (!content || !content.trim()) {
    alert('댓글을 작성해주세요.')
    return;
  }
  if(userStore.isLogin !== false){
    try {
      const response = await axios.post('http://localhost:80/reply/replyInsert', {
        cm_b_idx: bidx,
        cm_content: content,
        cm_parent_idx: parentIdx,
      })
      if(response.data === "success"){
         replyContentMap.value = { ...replyContentMap.value, [parentIdx]: '' };
         // ✅ 현재 열려 있는 답글 상태 저장
        const currentReplyToList = new Set(replyToList.value);
          // replyTo.value = null;
          // 특정 답글창만 닫기
          replyToList.value.delete(parentIdx);
          fetchPostComment();
          replyToList.value = new Set(currentReplyToList);
      } else {
        alert('대댓글 등록에 실패했습니다.')
      }
    } catch (err) {
      console.error('대댓글 등록 실패:', err);
    }
  }else{
    alert('로그인 후 이용해주세요.')
    return
  }
};

// 좋아요 토글버튼
const likeCount = ref(0);
const isLiked = ref(false);

// 게시글 좋아요 상태 가져오기
const fetchLikeStatus = async () => {
  try {
    const res = await axios.get('http://localhost:80/board/likeStatus', {
      params: {
        b_idx: bidx
      }
    });
    isLiked.value = !!res.data.liked; // 불린값으로 강제
    likeCount.value = typeof res.data.likeCount === 'number' ? res.data.likeCount : 0;
  } catch (err) {
    console.error('좋아요 상태 로딩 실패:', err);
    isLiked.value = false;
    likeCount.value = 0; // 실패 시 안전한 초기값
  }
};

// 비회원일때 좋아요 갯수
const fetchLikeCount = async () => {
  try {
    const res = await axios.get('http://localhost:80/board/likeCount', {
      params: { b_idx: bidx }
    });
    likeCount.value = typeof res.data.likeCount === 'number' ? res.data.likeCount : 0;
  } catch (err) {
    console.error('좋아요 수 로딩 실패:', err);
    likeCount.value = 0;
  }
};


// 좋아요 토글
const toggleLike = async () => {
  if (!userStore.isLogin) {
    alert('로그인 후 이용해주세요.');
    return;
  }

  const payload = {
    h_u_idx: userStore.userIdx,
    b_idx: bidx,
    b_u_idx: buidx.value,
    b_title: btitle.value
  };
  const deleteload = {
    h_u_idx: userStore.userIdx,
    b_idx: bidx
  };
  console.log("펭로드=>>",payload)
  try {
    if (isLiked.value) {
      await axios.post('http://localhost:80/board/likeDelete', deleteload);
      likeCount.value = Math.max(0, likeCount.value - 1); // 최소 0
    } else {
      await axios.post('http://localhost:80/board/likeInsert', payload);
      likeCount.value++;
    }
    isLiked.value = !isLiked.value;
  } catch (err) {
    console.error('좋아요 처리 실패:', err);
  }
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
  align-items: center;
  margin-bottom: 16px;
}
.post-meta-right {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-left: auto;
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
  color: #919191;
  font-weight: bold;
}

.board-images {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.board-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
  border-radius: 6px;
  border: 1px solid #ccc;
}
.like-button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
  color: #888;
  padding: 0;
}
.like-button .liked {
  color: red;
}
.action {
  cursor: pointer;
  user-select: none;
  color: #919191;
}
.liked {
  color: red;
}
.heart-icon {
  cursor: pointer;
  font-size: 15px;
  transition: color 0.2s;
}
.heart-icon.liked {
  color: red;
}
</style>
