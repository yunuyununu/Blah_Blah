<template>
  <div class="post-container" v-if="post">
    <!-- 게시글 내용 -->
    <div class="post-header">
      <h2 v-if="!isEditing">{{ post.B_TITLE }}</h2>
      <input v-else v-model="editedTitle" class="form-control" />
      <div v-if="titleError" class="text-danger" style="font-size: 12px;">
        {{ titleError }}
      </div>
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
      <p v-if="!isEditing" v-html="post.B_CONTENT"></p>
      <textarea v-else v-model="editedContent" class="form-control" rows="10"></textarea>
      <div v-if="isEditing" class="text-muted" style="font-size: 12px;">
        {{ editedContent.length }}자 입력됨
      </div>
      <div v-if="contentError" class="text-danger" style="font-size: 12px;">
        {{ contentError }}
      </div>
    </div>
    <div class="board-images" v-if="boardImages.length > 0 && previewImages.length === 0">
        <img
          v-for="img in boardImages"
          :key="img.I_IDX"
          :src="getImageUrl(img.I_IMAGE)"
          alt="게시글 이미지"
          class="board-image"
        />
      </div>
      <br>
      <!-- 이미지 업로드 버튼 (스타일 적용) -->
        <div class="file-upload-wrapper" v-if="isEditing && userStore.userIdx === post.B_U_IDX">
          <label for="fileInput" class="file-upload-label">
            이미지 선택
          </label>
          <input
            id="fileInput"
            type="file"
            multiple
            @change="onImageChange"
            accept=".jpg,.jpeg,.png,.pdf"
            class="file-input-hidden"
          />
        </div>

      <div class="image-preview" v-if="previewImages.length > 0">
        <img v-for="(img, i) in previewImages" :key="i" :src="img" />
        <br>
      </div>
    <br>
    <!-- 투표 -->
     <!-- 투표 게시글 표시 영역 -->
    <div v-if="post.B_VOTE === 'Y'" class="vote-container">
    <div class="vote-header">
      <div class="vote-icon"><svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-bar-chart-line-fill" viewBox="0 0 16 16">
  <path d="M11 2a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v12h.5a.5.5 0 0 1 0 1H.5a.5.5 0 0 1 0-1H1v-3a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v3h1V7a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1v7h1z"/>
</svg></div>
      <div class="vote-info">
        <h3 class="vote-title">투표 참여 {{ getTotalVotes() }}명</h3>
        <p class="vote-subtitle">하나만 선택할 수 있습니다</p>
      </div>
    </div>

    <div class="vote-options">
      <div 
        v-for="option in voteOptions" 
        :key="option.VO_IDX" 
        class="vote-option-item"
        :class="{ 'selected': selectedVoteOption === option.VO_IDX, 'disabled': hasVoted }"
        @click="!hasVoted && (selectedVoteOption = option.VO_IDX)"
      >
        <label class="vote-option-label">
          <input 
            type="radio"
            :value="option.VO_IDX"
            v-model="selectedVoteOption"
            :disabled="hasVoted"
            class="vote-radio"
          />
          <div class="vote-option-content">
            <span class="vote-option-text">{{ option.VO_CONTENT }}</span>
            <span v-if="hasVoted" class="vote-count">{{ option.VO_COUNT || 0 }}표</span>
          </div>
        </label>

        <!-- 투표 결과 바 -->
        <transition name="progress-fade">
          <div v-if="hasVoted" class="vote-progress">
            <div 
              class="vote-progress-bar" 
              :style="{ width: getVotePercentage(option.VO_COUNT || 0) + '%' }"
            ></div>
          </div>
        </transition>
      </div>
    </div>

    <div class="vote-actions">
      <button 
        v-if="!hasVoted && userStore.userIdx" 
        class="vote-submit-btn"
        :class="{ 'active': selectedVoteOption }"
        :disabled="!selectedVoteOption"
        @click="submitVote"
      >
        투표하기
      </button>
      <button 
        v-if="!userStore.userIdx"
        class="vote-submit-btn"
        disabled
      >
        투표는 회원만 가능합니다.
      </button>
      <div v-if="hasVoted" class="vote-completed">
        <span class="vote-completed-icon">✔️</span>
        <span class="vote-completed-text">투표가 완료되었습니다</span>
      </div>
    </div>
  </div>
<br>
<br>
    <button v-if="!isEditing" class="btn btn-outline-danger" @click="$router.back()"  style="text-align: left;">← 목록으로</button>
        <div v-if="userStore.userIdx === post.B_U_IDX" style="text-align:right">
          <template v-if="!isEditing">
            <span class="action" @click="updateBoard">✏️ 수정</span>&nbsp;&nbsp;
            <span class="action" @click="deleteBoard">🗑️ 삭제</span>
          </template>
          <template v-else>
            <button class="btn btn-primary" @click="saveUpdate">💾 저장</button>
            <button class="btn btn-secondary" @click="cancelUpdate">❌ 취소</button>
          </template>
        </div>
    <hr />

    <div class="comment-section">
        <h3>댓글 {{ commentList.length }}</h3>
        <textarea
          v-model="newComment"
          :disabled="!isLogin"
          :placeholder="isLogin ? '댓글을 남겨주세요.' : '로그인 후 댓글을 작성하실 수 있습니다.'"
          @input="validateNewComment"
        ></textarea>
        <!-- 글자 수 표시 영역 추가 -->
        <div class="comment-char-count" style="text-align: right; font-size: 12px; margin-top: 5px;">
          <span :style="{ color: newComment.length >= 300 ? '#d32f2f' : '#666' }">
            {{ newComment.length }}/300자
          </span>
        </div>
        <!-- 에러 메시지 -->
        <div v-if="newCommentError" class="error-message" style="color: #d32f2f; font-size: 12px; margin-top: 5px;">
          {{ newCommentError }}
        </div>
        <div class="comment-action">
          <button
            class="btn btn-outline-primary"
            @click="submitComment"
            :disabled="!isLogin"
          >
            등록
          </button>
        </div>
         <div class="comment-list" v-if="threadedComments.length > 0">
          <CommentItem 
            v-for="comment in threadedComments"
            :key="comment.cm_idx"
            :comment="comment"
            :reply-to-list="replyToList"
            :reply-content-map="replyContentMap"
            :post-writer-id="post.B_U_IDX"
            @toggle-reply="toggleReply"
            @submit-reply="submitReply"
            @update-reply-content="updateReplyContent"
            @delete-comment="handleDeleteComment"
            @save-edit="updateCommentContent"
          />
        </div>

        <div v-else style="text-align: center;">
          <p>작성된 댓글이 없습니다.</p>
        </div>
      </div>
  </div>
  <div v-else class="loading">불러오는 중...</div>
</template>

<script setup>
import CommentItem from '@/views/CommentItem.vue';
import { useUserStore } from '@/store/userStore'
// import { useAlarmStore } from '@/store/alarmStore'
import { useRouter } from 'vue-router'


// const alarmStore = useAlarmStore()
const userStore = useUserStore()
const isLogin = computed(() => userStore.isLogin)
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

// 게시글 수정
const files = ref([])
const previewImages = ref([])

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

    // 댓글 목록 갱신
    fetchPostComment(); // 댓글 목록 다시 불러오는 함수
  } catch (err) {
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
    return `https://storage.googleapis.com/blah_blah_bucket/board/${filename}`;
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

const isEditing = ref(false);
const editedTitle = ref('');
const editedContent = ref('');

const titleError = ref('');
const contentError = ref('');
const newCommentError = ref('');

const updateBoard = () => {
  isEditing.value = true;
  editedTitle.value = post.value.B_TITLE;
  editedContent.value = post.value.B_CONTENT;
}
const saveUpdate = async () => {
  titleError.value = '';
  contentError.value = '';

  if (editedTitle.value.trim().length === 0) {
    titleError.value = '제목을 입력해주세요.';
  }
  if (editedContent.value.trim().length === 0) {
    contentError.value = '내용을 입력해주세요.';
  }

  if (titleError.value || contentError.value) {
    // 에러가 있으면 저장 중단
    return;
  }

    if(files.value.length === 0) {
    try {
      await axios.post('http://localhost:80/board/boardUpdate', {
        b_idx: bidx,
        b_title: editedTitle.value,
        b_content: editedContent.value,
      });
        isEditing.value = false;
        await fetchPostDetail(); // 다시 로드
  
    } catch (err) {
      console.error('게시글 수정 실패:', err);
    }
  }else {
    const formData = new FormData()
    formData.append('b_idx', bidx)
    formData.append('b_title', editedTitle.value)
    formData.append('b_content', editedContent.value)
    files.value.forEach(file => formData.append('images', file))

    // 기존 이미지 파일명도 같이 보냄
    boardImages.value.forEach(img => {
      formData.append('originalImages', img.I_IMAGE);
    });

      await axios.post('http://localhost:80/board/boardImageUpdate',formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          },
          withCredentials: true
      });
        isEditing.value = false;
        await fetchPostDetail(); // 다시 로드
  }
};

const cancelUpdate = () => {
  isEditing.value = false;
  previewImages.value = [];
  files.value = [];
  // 제목, 내용도 원래 글로 되돌리기
  editedTitle.value = post.value.B_TITLE;
  editedContent.value = post.value.B_CONTENT;
  titleError.value = '';
  contentError.value = '';
};

const deleteBoard = async () => {

  const formData = new FormData()
    formData.append('b_idx', bidx)

    // 기존 이미지 보내기
    boardImages.value.forEach(img => {
      formData.append('originalImages', img.I_IMAGE);
    });


  try {
    const confirmed = window.confirm('정말 이 게시물을 삭제하시겠습니까?');
    if (!confirmed) return;
    await axios.post(`http://localhost:80/board/boardDelete`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          },
          withCredentials: true
      });
    router.push('/board/boardlist')
  } catch (err) {
    console.error('게시글 삭제 실패:', err);
  }
};

onMounted(async () => {
  fetchPostDetail();
  fetchPostComment();
  fetchVoteOptions();
  
  if (userStore.isLogin) {
    fetchLikeStatus();
    checkUserVoteStatus();
    
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

// 새 댓글 검증
function validateNewComment() {
  const length = newComment.value.length;
  if (length === 0) {
    newCommentError.value = '';
  } else if (length > 300) {
    newCommentError.value = '댓글은 300자 이하로 입력해주세요.';
  } else {
    newCommentError.value = '';
  }
}

const submitComment = async() => {
  if(userStore.isLogin === false){
    alert('로그인 후 이용가능합니다.')
    return
  }
  
  const trimmedComment = newComment.value.trim();
  if (trimmedComment.length === 0) {
    newCommentError.value = '댓글을 작성해주세요.';
    return;
  }
  if (trimmedComment.length > 300) {
    newCommentError.value = '댓글은 300자 이하로 입력해주세요.';
    return;
  }
  
  // 에러가 없으면 에러 메시지 초기화
  newCommentError.value = '';
  
  if(userStore.isLogin !== false){
    try {
      const response = await axios.post('http://localhost:80/reply/commentInsert', {
        cm_b_idx: bidx,
        cm_content: newComment.value,
      })
      if(response.data === "success"){
        newComment.value = '';
        newCommentError.value = ''; // 성공 시 에러 메시지 초기화
        fetchPostComment();
      }
    } catch (err) {
      console.error('댓글 등록 실패:', err);
    }
  }else{
    alert('로그인 후 이용해주세요.')
    return
  }
};

const updateCommentContent = async (cmIdx, newContent) => {
  try {
    await axios.post('http://localhost:80/reply/commentUpdate', {
      cm_idx: cmIdx,
      cm_content: newContent
    });
    fetchPostComment();
    // 필요한 경우 comments를 다시 fetch하거나 수정된 항목만 업데이트
  } catch (err) {
    console.error("댓글 수정 실패", err);
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

const onImageChange = (event) => {
  const filesSelected = Array.from(event.target.files);

  // 기존 이미지 개수 + 새로 업로드할 이미지 개수가 10을 초과하면 경고
  if (files.value.length + filesSelected.length > 10) {
    alert(`이미지는 최대 10개까지 업로드할 수 있습니다.`);
    return;
  }

  // 파일 미리보기 추가
  filesSelected.forEach(file => {
    files.value.push(file);
    const reader = new FileReader();
    reader.onload = (e) => {
      previewImages.value.push(e.target.result);
    };
    reader.readAsDataURL(file);
  });
};

// 투표
const voteOptions = ref([]);
const selectedVoteOption = ref(null);
const hasVoted = ref(false);

// 투표 항목 불러오기
const fetchVoteOptions = async () => {
  // if (post.value.B_VOTE !== 'N') return;

  try {
    const res = await axios.get(`http://localhost:80/board/voteInfo`, {
      params: { v_b_idx: bidx },
    });

    voteOptions.value = res.data;
  } catch (err) {
    console.error('투표 옵션 불러오기 실패:', err);
  }
};

// 투표 제출
const submitVote = async () => {
  if (!selectedVoteOption.value) {
    alert('투표 항목을 선택해주세요.');
    return;
  }
  try {
    await axios.post(`http://localhost:80/board/votePick`, {}, {
      params: {
        vr_vo_idx: selectedVoteOption.value
      }
    });

    hasVoted.value = true;
    await fetchVoteOptions(); // 투표 후 결과 다시 로드
  } catch (err) {
    console.error('투표 제출 실패:', err);
  }
};

// 사용자 투표 상태 확인
const checkUserVoteStatus = async () => {
  if (!userStore.isLogin) return;
  
  try {
    const res = await axios.get('http://localhost:80/board/voteCheck', {
      params: { 
        v_b_idx: bidx
      },
    });

    const votedOptionIdx = res.data;
    
    if (votedOptionIdx > 0) { // 투표한 경우 (0보다 큰 값)
      hasVoted.value = true;
      selectedVoteOption.value = votedOptionIdx;
    } else { // 투표하지 않은 경우 (0 또는 음수)
      hasVoted.value = false;
      selectedVoteOption.value = null;
    }
    
  } catch (err) {
    console.error('투표 상태 확인 실패:', err);
  }
};

// 총 투표 수 계산
const getTotalVotes = () => {
  return voteOptions.value.reduce((total, option) => total + (option.VO_COUNT || 0), 0)
};

// 투표 비율 계산
const getVotePercentage = (count) => {
  const total = getTotalVotes();
  return total > 0 ? Math.round((count / total) * 100) : 0;
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
  resize: none;
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
  font-size: 17px;
  transition: color 0.2s;
}
.heart-icon.liked {
  color: red;
}
.image-preview {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10px;
  gap: 10px;
}

.image-preview img {
  width: 200px;
  height: 200px;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.3s ease;
}

.image-preview img:hover {
  opacity: 0.7;
}

.vote-container {
  max-width: 480px;
  margin: 0 auto;
  padding: 24px 20px;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  font-family: 'Noto Sans KR', sans-serif;
  user-select: none;
}

.vote-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 18px;
}

.vote-icon {
  font-size: 28px;
}

.vote-info {
  flex: 1;
}

.vote-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #222;
}

.vote-subtitle {
  margin: 4px 0 0 0;
  font-size: 13px;
  color: #666;
}

.vote-options {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.vote-option-item {
  cursor: pointer;
  border: 1.8px solid #ddd;
  border-radius: 10px;
  padding: 14px 18px;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
  background-color: #fafafa;
  display: flex;
  flex-direction: column;
  user-select: none;
}

.vote-option-item.selected {
  border-color: #3b82f6;
  background-color: #e0f0ff;
  box-shadow: 0 0 8px rgba(59,130,246,0.3);
}

.vote-option-item.disabled {
  cursor: default;
  opacity: 0.7;
}

.vote-option-label {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
  cursor: pointer;
}

.vote-radio {
  appearance: none;
  -webkit-appearance: none;
  width: 22px;
  height: 22px;
  border: 2px solid #bbb;
  border-radius: 50%;
  position: relative;
  cursor: pointer;
  transition: border-color 0.3s ease;
  flex-shrink: 0;
}

.vote-radio:checked {
  border-color: #3b82f6;
  background-color: #3b82f6;
}

.vote-radio:checked::after {
  content: "";
  position: absolute;
  top: 5px;
  left: 5px;
  width: 8px;
  height: 8px;
  background: white;
  border-radius: 50%;
}

.vote-option-content {
  flex-grow: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.vote-option-text {
  font-size: 15px;
  color: #333;
  font-weight: 500;
}

.vote-count {
  font-size: 13px;
  color: #666;
  font-weight: 600;
  min-width: 40px;
  text-align: right;
}

.vote-progress {
  height: 8px;
  margin-top: 8px;
  background: #e5e7eb;
  border-radius: 6px;
  overflow: hidden;
}

.vote-progress-bar {
  height: 100%;
  background: #3b82f6;
  border-radius: 6px 0 0 6px;
  transition: width 0.6s ease;
}

.vote-actions {
  margin-top: 20px;
  text-align: center;
}

.vote-submit-btn {
  background-color: #cbd5e1;
  border: none;
  color: #777;
  padding: 12px 28px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 30px;
  cursor: not-allowed;
  transition: background-color 0.3s ease, color 0.3s ease;
  user-select: none;
}

.vote-submit-btn.active {
  background-color: #3b82f6;
  color: white;
  cursor: pointer;
}

.vote-submit-btn:disabled {
  pointer-events: none;
}

.vote-completed {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  color: #16a34a;
  font-weight: 700;
  font-size: 16px;
}

.vote-completed-icon {
  font-size: 22px;
}

/* 애니메이션 효과 */
.progress-fade-enter-active,
.progress-fade-leave-active {
  transition: opacity 0.4s ease;
}
.progress-fade-enter-from,
.progress-fade-leave-to {
  opacity: 0;
}
button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.file-upload-wrapper {
  margin: 10px 0;
}

.file-upload-label {
  display: inline-block;
  padding: 10px 20px;
  background-color: #3b82f6;
  color: white;
  font-weight: 600;
  font-size: 14px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.file-upload-label:hover {
  background-color: #2563eb;
}

.file-input-hidden {
  display: none;
}

</style>
