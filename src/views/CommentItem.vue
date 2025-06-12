<template>
  <div 
    class="comment-item" 
    :class="{ 'reply-comment': comment.cm_parent_idx }"
    :style="{ marginLeft: comment.cm_parent_idx ? '20px' : '0' }"
  >
    <div class="comment-header">
      <span class="nickname">
        <span class="nickname-blue">{{ comment.c_nicname }}</span> · 
        <span class="nickname-gray">{{ comment.u_nicname }}
          <span v-if="comment.cm_u_idx === postWriterId" class="author-tag">(작성자)</span>
        </span>
      </span>
      <span class="comment-date">{{ comment.cm_date }}</span>
    </div>

     <!-- 수정 중일 때 textarea 표시 -->
    <div class="comment-body comment-edit-form" v-if="isEditing">
      <textarea
        v-model="editedContent"
        class="edit-textarea"
        @input="validateEdit"
      ></textarea>
      <!-- 글자 수 -->
      <div class="char-count">{{ editedContent.length }}자</div>
      <!-- 에러 메시지 -->
      <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
      <div class="edit-actions">
        <button class="btn btn-sm btn-primary" @click="saveEdit">저장</button>
        <button class="btn btn-sm btn-secondary" @click="cancelEdit">취소</button>
      </div>
    </div>

    <!-- 기본 상태일 때 내용 표시 -->
    <div class="comment-body" v-else>{{ comment.cm_content }}</div>
    
    <!-- 댓글만 답글 버튼 표시 -->
    <div v-if="!comment.cm_parent_idx" class="comment-footer">
        <span class="action" @click="$emit('toggle-reply', comment.cm_idx)">
          💬 답글
          <span v-if="comment.replies && comment.replies.length > 0" class="reply-count">
            ({{ comment.replies.length }})
          </span>
        </span>
      </div>

        <!-- 수정/삭제 버튼: 댓글/대댓글 모두 표시 -->
        <div v-if="userStore.userIdx === comment.cm_u_idx" class="comment-actions">
          <span class="action" @click="startEdit" v-if="!isEditing">✏️ 수정</span>
          <span class="action" @click="$emit('delete-comment', comment.cm_idx)">🗑️ 삭제</span>
        </div>

    <!-- 답글 입력창 및 대댓글 리스트 (replyTo일 때만 보여줌) -->
    <div v-if="replyToList.has(comment.cm_idx)" class="reply-area">
      <div class="reply-form">
        <textarea 
          :value="replyContentMap[comment.cm_idx] || ''"
          @input="handleReplyInput"
          placeholder="답글을 입력하세요."
        ></textarea>
        <!-- 답글 글자 수 표시 -->
        <div class="reply-char-count">{{ (replyContentMap[comment.cm_idx] || '').length }}자</div>
        <!-- 답글 에러 메시지 -->
        <div v-if="replyErrorMessage" class="error-message">{{ replyErrorMessage }}</div>
        <div class="reply-actions">
          <button class="btn btn-sm btn-primary" @click="$emit('submit-reply', comment.cm_idx)">
            등록
          </button>
        </div>
      </div>
      <br>

      <!-- 대댓글 리스트 재귀 렌더링 -->
      <div class="replies-list">
        <CommentItem 
          v-for="child in (Array.isArray(comment.replies) ? comment.replies : [])"
          :key="child.cm_idx"
          :comment="child"
          :reply-to-list="replyToList"
          :reply-content-map="replyContentMap"
          :post-writer-id="postWriterId"
          @toggle-reply="$emit('toggle-reply', $event)"
          @submit-reply="$emit('submit-reply', $event)"
          @update-reply-content="$emit('update-reply-content', $event, arguments[1])"
          @delete-comment="$emit('delete-comment', $event)"
          @save-edit="(cmIdx, content) => $emit('save-edit', cmIdx, content)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, ref } from 'vue';
import { useUserStore } from '@/store/userStore'

const userStore = useUserStore()

const props = defineProps({
  comment: Object,
  postWriterId: Number,
  replyToList: Object,
  replyContentMap: Object
});

// defineEmits로 이벤트 정의
const emit = defineEmits([
  'toggle-reply',
  'submit-reply', 
  'update-reply-content',
  'delete-comment',
  'save-edit'
]);

// 수정 상태 변수
const isEditing = ref(false);
// 수정 내용 임시 저장
const editedContent = ref('');

const errorMessage = ref('')
const replyErrorMessage = ref('')

// 답글 입력 처리
function handleReplyInput(event) {
  const content = event.target.value;
  emit('update-reply-content', props.comment.cm_idx, content);
  validateReply(content);
}

// 답글 유효성 검사
function validateReply(content) {
  const length = content.trim().length;
  if (length === 0) {
    replyErrorMessage.value = '';
  } else if (length > 300) {
    replyErrorMessage.value = '답글은 300자 이하로 입력해주세요.';
  } else {
    replyErrorMessage.value = '';
  }
}

// 수정 시작
function startEdit() {
  isEditing.value = true;
  editedContent.value = props.comment.cm_content;
}

// 수정 저장
function saveEdit() {
  const length = editedContent.value.length;

  if (length === 0) {
    errorMessage.value = '내용을 입력해주세요.';
    return;
  }

  if (length > 300) {
    errorMessage.value = '댓글은 300자 이하로 입력해주세요.';
    return;
  }

  // 순환 참조를 피하기 위해 필요한 값만 전달
  const cmIdx = props.comment.cm_idx;
  const content = editedContent.value.trim();
  
  emit('save-edit', cmIdx, content);
  isEditing.value = false;
  errorMessage.value = '';
}

// 수정 취소
function cancelEdit() {
  isEditing.value = false;
  errorMessage.value = '';
}

function validateEdit() {
  const length = editedContent.value.trim().length;
  if (length === 0) {
    errorMessage.value = '내용을 입력해주세요.';
  } else if (length > 300) {
    errorMessage.value = '댓글은 300자 이하로 입력해주세요.';
  } else {
    errorMessage.value = '';
  }
}
</script>

<style scoped>
.comment-item {
  border-bottom: 1px solid #ddd;
  padding: 12px 16px;
  margin-bottom: 8px;
  background-color: white;
}

.reply-comment {
  background-color: #f7f7f7;
  /* border-radius: 8px; */
  /* padding: 12px; */
  margin-left: 20px;
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
  word-break: break-word; 
  overflow-wrap: break-word; 
}
.comment-footer {
  font-size: 13px;
  color: #888;
  display: flex;
  justify-content: space-between;
  margin-top: 4px;
  align-items: center;
}
.comment-edit-form {
  margin-top: 8px;
  margin-bottom: 8px;
  padding: 10px;
  background-color: #fff;
}

.edit-textarea {
  width: 100%;
  height: 60px;
  padding: 8px;
  resize: none;
  /* border: 1px solid #ddd;
  border-radius: 4px; */
  margin-bottom: 8px;
}

.edit-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end; /* 버튼들을 오른쪽 정렬 */
}

.char-count {
  font-size: 12px;
  color: #666;
  text-align: right;
  margin-bottom: 4px;
}

.error-message {
  color: #e74c3c;
  font-size: 13px;
  margin-bottom: 4px;
}
.comment-actions {
  display: flex;
  gap: 12px;
  justify-content: flex-end;
  margin-top: 4px;
}
.reply-form {
  margin-top: 8px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  border: 1px solid #ccc; /* 테두리 추가 */
  border-radius: 6px;
  padding: 10px;
  background-color: #fff;
}

.reply-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.reply-char-count {
  font-size: 12px;
  color: #666;
  text-align: right;
  margin-top: -4px;
}

textarea {
  width: 100%;
  height: 60px;
  padding: 8px;
  resize: none;
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

.action {
  cursor: pointer;
  user-select: none;
  color: #919191;
}
.action:hover {
  text-decoration: underline;
}
.reply-count {
  color: #919191;
  font-weight: bold;
  margin-left: 4px;
  font-size: 12px;
}

.author-tag {
  font-size: 12px;
  color: #e46868;
  margin-left: 4px;
}
</style>