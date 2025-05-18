<template>

    <main class="main-content">
      <div class="form-grid">
        <div class="form-group">
          <label>아이디</label>
          <input type="text" readonly />
        </div>

        <div class="form-group">
          <label>비밀번호</label>
          <button type="button" class="change-password-btn" @click="openModal">비밀번호 변경</button>
        </div>

        <div class="form-group">
          <label>이름</label>
          <input type="text" />
        </div>

        <div class="form-group">
          <label>이메일</label>
          <input type="email" />
        </div>

        <div class="form-group">
          <label>생년월일</label>
          <input type="text" />
        </div>

        <div class="form-group">
          <label>전화번호</label>
          <input type="number" />
        </div>

        <div class="form-group">
          <label>회원회사</label>
          <input type="text" />
        </div>

        <div class="form-group company-change">
          <label>회사인증파일</label>
          <input type="text"/>
          <button class="company-change-btn">회사 변경 신청</button>
        </div>

        <div class="form-group">
          <label>회원가입일자</label>
          <input type="date"/>
        </div>
      </div>

      <div class="submit-btn-wrapper">
        <button class="btn btn-dark">회원 정보 수정</button>
      </div>
    </main>


    <div v-if="showModal" class="modal-overlay">
      <div class="modal">
        <h2>비밀번호 변경</h2>
        <input
          type="password"
          placeholder="새 비밀번호"
          v-model="newPassword"
          class="modal-input"
        />
        <input
          type="password"
          placeholder="새 비밀번호 확인"
          v-model="confirmPassword"
          class="modal-input"
        />
        <div class="modal-buttons">
          <button class="modal-btn" @click="changePassword">변경</button>
          <button class="modal-btn cancel" @click="closeModal">취소</button>
        </div>
      </div>
    </div>
  
  </template>
  
  <script setup>
  import { ref } from 'vue';

// const form = reactive({
//   id: 'Jane',
//   name: 'Jane',
//   birth: '1995-01-01',
//   company: 'Smitherton',
//   companyFile: '',
//   email: 'smitherton@example.com',
//   phone: '010-1234-5678',
//   signupDate: '2025-01-01',
// });

// 모달 제어
const showModal = ref(false);
const newPassword = ref('');
const confirmPassword = ref('');

const openModal = () => {
  console.log('모달 열기 시도'); // 추가
  showModal.value = true;
  console.log('showModal:', showModal.value); // 추가
};

const closeModal = () => {
  showModal.value = false;
  newPassword.value = '';
  confirmPassword.value = '';
};

const changePassword = () => {
  if (newPassword.value !== confirmPassword.value) {
    alert('비밀번호가 일치하지 않습니다.');
    return;
  }
  // 여기서 비밀번호 변경 API 호출하면 됨
  alert('비밀번호가 변경되었습니다.');
  closeModal();
};
  
  </script>
  
  <style scoped>
.sidebar {
  width: 200px;
  padding-right: 40px;
  border-right: 1px solid #ddd;
}

.sidebar h1 {
  font-size: 28px;
  margin-bottom: 20px;
}

.sidebar h2 {
  font-size: 20px;
  margin-bottom: 10px;
}

.sidebar h3 {
  font-size: 16px;
  color: #666;
}

.main-content {
  flex: 1;
  padding-left: 40px;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 23px 43px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 8px;
  font-weight: bold;
}

input[type="text"],
input[type="email"],
input[type="date"],
input[type="number"] {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.change-password-btn,
.company-change-btn {
  margin-top: 8px;
  padding: 8px 12px;
  background-color: black;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.company-change {
  display: flex;
  flex-direction: column;
}

.submit-btn-wrapper {
  margin-top: 40px;
  text-align: right;
}

.submit-btn {
  padding: 10px 20px;
  background-color: black;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

/* 기존 스타일은 생략 (필요하면 위에서 참고) */

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.modal {
  background: white;
  padding: 30px;
  border-radius: 8px;
  width: 300px;
  text-align: center;
}

.modal h2 {
  margin-bottom: 20px;
}

.modal-input {
  width: 100%;
  padding: 8px;
  margin-bottom: 12px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.modal-buttons {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.modal-btn {
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  background-color: black;
  color: white;
  cursor: pointer;
}

.modal-btn.cancel {
  background-color: #ccc;
  color: black;
}
  </style>