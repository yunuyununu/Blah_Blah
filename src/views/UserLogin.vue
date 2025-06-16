<template>
  <div class="content">
    <div class="container">
      <form @submit.prevent="submit">
        <!-- 제목 -->
        <div class="row">
          <div class="col" style="text-align: center;">
            <h2>로그인</h2><br><br>
          </div>
        </div>

        <!-- 아이디 -->
        <div class="row">
          <div class="col" style="text-align: center;">
            <br>
            <label>&nbsp;&nbsp;&nbsp;&nbsp;아이디</label>&nbsp;&nbsp;
            <input
              type="text"
              v-model="userId"
              ref="userIdInput"
              :class="['custom-input', { 'input-error': errors.userId }]"
            />&nbsp;&nbsp;
            <p v-if="errors.userId" class="error-text">{{ errors.userId }}</p>
          <br>
            <br>
          </div>
        </div>

        <!-- 비밀번호 -->
        <div class="row">
          <div class="col" style="text-align: center;">
            <br>
            <label>비밀번호</label>&nbsp;&nbsp;
            <input
              type="password"
              v-model="userPw"
              ref="userPwInput"
              :class="['custom-input', { 'input-error': errors.userPw }]"
            />
            <p v-if="errors.userPw" class="error-text">{{ errors.userPw }}</p>
          <br>
            <br>
          </div>
        </div>

        <!-- 로그인 에러 메시지 -->
        <div v-if="loginError" class="row">
          <div class="col" style="text-align: center;">
            <p class="error-text">{{ loginError }}</p>
          </div>
        </div>

        <!-- 로그인 버튼 -->
        <div class="row">
          <div class="col" style="text-align: center;">
            <br>
            <button type="submit" class="btn btn-outline-dark">로그인</button>&nbsp;&nbsp;
            <br><br>
          </div>
        </div>

        <!-- 기타 -->
        <div class="row">
          <div class="col" style="text-align: center;">
            <br>
            <button type="button" class="btn btn-light" @click="goSearchId">아이디 찾기</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-light" @click="goSearchPw">비밀번호 찾기</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-danger" @click="goJoin">회원가입</button>
          <br><br>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

  <script setup>
  import { useRouter } from 'vue-router';
  import { useUserStore } from '@/store/userStore'

  const router = useRouter();
   import { ref, watch } from 'vue'
   import axios from 'axios'
  const goJoin = () => router.push('/join');
  const goSearchId = () => router.push('/searchid');
  const goSearchPw = () => router.push('/searchpw');

  const userId = ref('')
  const userPw = ref('')

  const userIdInput = ref(null)
  const userPwInput = ref(null)

const loginError = ref('')

  const errors = ref({
        userId : '',
        userPw : ''
      })
  
  const userStore = useUserStore()

const submit = async () => {
loginError.value = ''
  let isValid = true

  // 아이디 체크
  if (!userId.value) {
    errors.value.userId = '아이디를 입력하세요.'
    if (isValid) userIdInput.value.focus()
    return
  } 

  // 비밀번호 체크
  if (!userPw.value) {
    errors.value.userPw = '비밀번호를 입력하세요.'
    if (isValid) userPwInput.value.focus()
    return
  }

  try {
    const response = await axios.post('http://localhost:80/login/userlogin', {
       userId : userId.value,
       userPw : userPw.value
    }, { withCredentials: true }) // 세션유지
    if (response.data.result === 'success') {
        userStore.setLoginSuccess(response.data.userIdx);
      console.log('로그인 응답:', response.data)
       router.push('/')
     } else if(response.data.result === 'withdraw') {
      alert('탈퇴한 회원입니다.\n홈페이지를 이용하시려면 회원가입이 필요합니다.')
      return
      } else { // 로그인 정보 불일치
       loginError.value = '아이디 또는 비밀번호가 일치하지 않습니다.';
     }
  } catch (error) {
     console.error('로그인 전송 실패: '+error)
  }
 }
  
watch(userId, () => {
    errors.value.userId = ''
  })
watch(userPw, () => {
    errors.value.userPw = ''
  })
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

.row {
  gap: 23px 43px;
}

.row2 {
  gap: 23px 43px;
  /* margin-top: 4px;
  margin-bottom: 4px; */
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
input[type="number"],
input[type="password"] {
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

.custom-input::placeholder {
  font-size: 12px;   /* 크기 */
  color: #a3a2a2;       /* 색상 */
  font-style: italic; /* 스타일 */
}
.input-error {
  border: 2px solid red !important;
}

.error-text {
  color: red;
  font-size: 12px;
  margin-top: 3px;
  margin-bottom: 8px;
}
  </style>