<template>
  <div 
    class="comment-item" 
    :class="{ 'reply-comment': comment.cm_parent_idx }"
    :style="{ marginLeft: comment.cm_parent_idx ? '20px' : '0' }"
  >
    <div class="comment-header">
      <span class="nickname">
        <span class="nickname-blue">{{ comment.c_nicname }}</span> · 
        <span class="nickname-gray">{{ comment.u_nicname }}</span>
      </span>
      <span class="comment-date">{{ comment.cm_date }}</span>
    </div>

     <!-- 수정 중일 때 textarea 표시 -->
    <div class="comment-body" v-if="isEditing">
      <textarea v-model="editedContent" class="edit-textarea"></textarea>
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
          <span class="action" @click="startEdit" v-if="!isEditing && !comment.cm_parent_idx">✏️ 수정</span>
          <span class="action" @click="$emit('delete-comment', comment.cm_idx)">🗑️ 삭제</span>
        </div>

    <!-- 답글 입력창 및 대댓글 리스트 (replyTo일 때만 보여줌) -->
    <div v-if="replyToList.has(comment.cm_idx)" class="reply-area">
      <div class="reply-form">
        <textarea 
          :value="replyContentMap[comment.cm_idx] || ''"
          @input="$emit('update-reply-content', comment.cm_idx, $event.target.value)"
          placeholder="답글을 입력하세요."
        ></textarea>
        <button class="btn btn-sm btn-primary" @click="$emit('submit-reply', comment.cm_idx)">
          등록
        </button>
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
          @toggle-reply="$emit('toggle-reply', $event)"
          @submit-reply="$emit('submit-reply', $event)"
          @update-reply-content="$emit('update-reply-content', ...arguments)"
          @delete-comment="$emit('delete-comment', $event)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, ref,getCurrentInstance } from 'vue';
import { useUserStore } from '@/store/userStore'

const userStore = useUserStore()

const props = defineProps({
  comment: Object,
  replyToList: Object,
  replyContentMap: Object
});

// 수정 상태 변수
const isEditing = ref(false);
// 수정 내용 임시 저장
const editedContent = ref('');

// emit 객체를 setup context에서 가져오기
const internalInstance = getCurrentInstance();
const emit = internalInstance?.emit; // 타입 보호

// 수정 시작
function startEdit() {
  isEditing.value = true;
  editedContent.value = props.comment.cm_content;
}

// 수정 저장
function saveEdit() {
  // 부모 컴포넌트에 이벤트로 전달
  // 'save-edit' 이벤트로 cm_idx와 수정된 내용을 전달
  emit('save-edit', props.comment.cm_idx, editedContent.value);
  isEditing.value = false;
}

// 수정 취소
function cancelEdit() {
  isEditing.value = false;
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
}
.comment-footer {
  font-size: 13px;
  color: #888;
  display: flex;
  justify-content: space-between; /* 버튼을 오른쪽 끝으로 밀기 위함 */
  margin-top: 4px;
  align-items: center;
}
.comment-actions {
  display: flex;
  gap: 12px;
}
.reply-form {
  margin-top: 8px;
  display: flex;
  gap: 8px;
  align-items: flex-start;
  border: 1px solid #ccc; /* 테두리 추가 */
  border-radius: 6px;
  padding: 10px;
  background-color: #fff;
}
textarea {
  width: 100%;
  min-height: 60px;
  padding: 8px;
  resize: vertical;
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
.edit-textarea {
  width: 100%;
  min-height: 60px;
  padding: 8px;
  resize: vertical;
  margin-bottom: 8px;
}

.edit-actions {
  display: flex;
  gap: 8px;
}

</style>
