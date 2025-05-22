<template>
  <div class="content">
    <div class="container">

     <div class="row">
        <div class="col" style="text-align: center;">
          <h2>비밀번호 찾기</h2>
          <br><br>
        </div>
      </div>

      <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>이메일</label>
          </div>
          <div class="col-6">
            <input type="email" v-model="email" ref="emailInput" placeholder="useremail@example.com"
            :class="['custom-input', { 'input-error': errors.email  }]"/>
            &nbsp;&nbsp;
            <button type="button" class="btn btn-dark" @click="sendAuthCode">인증번호 전송</button>
            <p v-if="errors.email" class="error-text">{{ errors.email }}</p>
          </div>
          <div class="col-2"></div>
        </div>

        <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>인증번호</label>
          </div>
          <div class="col-6">
            <input type="text" v-model="authcode" ref="authcodeInput" placeholder="인증코드 6자리"
            :class="['custom-input', { 'input-error': errors.authcode  }]"/>&nbsp;&nbsp;
            <button type="button" class="btn btn-dark" @click="authcodeVerify">인증번호확인</button>&nbsp;&nbsp;
            <p v-if="errors.authcode" class="error-text">{{ errors.authcode }}</p>
            <!-- 타이머 -->
            <div v-if="timerVisible" style="margin-top: 3px;">
                <p style="font-weight: bold;">남은 시간: {{ formatTime() }}</p>
            </div>
            <!-- 인증 결과 메시지 -->
            <div v-if="verifyMessage" style="margin-top: 3px;">
                <p :style="{ color: verifySuccess === true ? 'blue' : verifySuccess === false ? 'red' : 'black' }">
                  {{ verifyMessage }}
                </p>
            </div>
          </div>
          <div class="col-2">
          </div>
        </div>
        <div class="row">
        <div class="col-2"></div>
        <div class="col-2">
          <label>아이디</label>
          <br>
        </div>
        <div class="col-6" style="gap: 5px;">
          <input type="text"  v-model="userId" ref="userIdInput" placeholder="영문,숫자 공백없이 6~12자리"
           :class="['custom-input', { 'input-error': errors.userId  }]"/>
          <p v-if="errors.userId" class="error-text">{{ errors.userId }}</p>
          <div v-if="idCheckMessage" style="margin-top: 5px; color: green;">{{ idCheckMessage }}</div>
        </div>
        <div class="col-2"></div></div>
        <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>전화번호</label>
          </div>
          <div class="col-6">
            <input type="text" v-model="userTel"
            ref="userTelInput"
            placeholder="숫자만 입력하세요."
            @input="formatPhoneNumber"
            :class="['custom-input', { 'input-error': errors.userTel }]"/>
            <p v-if="errors.userTel" class="error-text">{{ errors.userTel }}</p>
          </div>
          <div class="col-2"></div>
        </div>

        <div class="row">
          <div class="col" style="text-align: center;">
            <br>
            <button v-if="isCodeVerified" class="btn btn-danger" @click="resetPassword">비밀번호 재설정</button>
            <br>
            <br>
        </div>
        </div>

  </div>
</div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const email = ref('')
const authcode = ref('')
const userId = ref('')
const userTel = ref('')

const emailInput = ref(null)
const authcodeInput = ref(null)
const userIdInput = ref(null)
const userTelInput = ref(null)

const verifyMessage = ref('')
const verifySuccess = ref(null)

const countdown = ref(180)
const timer = ref(null)
const timerVisible = ref(false)

const errors = ref({
  email: '',
  authcode: '',
  userId: '',
  userTel: ''
})

const idCheckMessage = ref('')
const isCodeVerified = ref(false)

const sendAuthCode = async () => {
  errors.value.email = ''
  if (!email.value) {
    errors.value.email = '이메일을 입력하세요.'
    emailInput.value.focus()
    return
  }

  try {
    const response = await axios.post('http://localhost:80/join/pwEmailsend', {
      email: email.value
    })

    if (response.data === 'success') {
      alert('인증코드가 이메일로 전송되었습니다.')
      startTimer()
      timerVisible.value = true
    } else {
      errors.value.email = '등록되지 않은 이메일입니다.'
        emailInput.value.focus()
        return
    }
  } catch (err) {
    console.error(err)
    alert('서버 오류')
  }
}

const startTimer = () => {
  countdown.value = 180
  timer.value = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) {
      clearInterval(timer.value)
      timerVisible.value = false
    }
  }, 1000)
}

const formatTime = () => {
  const minutes = Math.floor(countdown.value / 60)
  const seconds = countdown.value % 60
  return `${minutes}:${seconds.toString().padStart(2, '0')}`
}

const authcodeVerify = async () => {
  errors.value.authcode = ''
  if (!authcode.value) {
    errors.value.authcode = '인증번호를 입력하세요.'
    authcodeInput.value.focus()
    return
  }

  try {
    const response = await axios.post('http://localhost:80/join/authCodeVerify', {
      email: email.value,
      authcode: authcode.value
    })

    if (response.data === 'success') {
      verifyMessage.value = '이메일 인증 성공'
      timerVisible.value = false
      verifySuccess.value = true
      isCodeVerified.value = true
    } else {
      verifyMessage.value = '인증번호가 올바르지 않습니다.'
      verifySuccess.value = false
    }
  } catch (err) {
    console.error(err)
    alert('서버 오류')
  }
}

const formatPhoneNumber = () => {
  let digits = userTel.value.replace(/\D/g, '') // 숫자만 추출

  // 최대 11자리까지만 허용
  if (digits.length > 11) {
    digits = digits.slice(0, 11)
  }

  if (digits.length < 4) {
    userTel.value = digits
  } else if (digits.length < 8) {
    userTel.value = `${digits.slice(0, 3)}-${digits.slice(3)}`
  } else {
    userTel.value = `${digits.slice(0, 3)}-${digits.slice(3, 7)}-${digits.slice(7)}`
  }
}

const resetPassword = async () => {
  errors.value.userId = ''
  errors.value.userTel = ''

  if (!userId.value) {
    errors.value.userId = '아이디를 입력하세요.'
    userIdInput.value.focus()
    return
  }
  if (!userTel.value) {
    errors.value.userTel = '전화번호를 입력하세요.'
    userTelInput.value.focus()
    return
  }

  try {
    const response = await axios.post('http://localhost:80/login/searchPw', {
      email: email.value,
      userId: userId.value,
      userTel: userTel.value
    })
    if (response.data) {
      router.push({ path: '/passwdreset', query: { u_idx: response.data.u_idx, u_id: response.data.u_id } })
      console.log("보내는 idx=="+response.data.u_idx)
      console.log("보내는 id=="+response.data.u_id)
    } else {
      alert('일치하는 회원 정보가 없습니다.')
    }
  } catch (err) {
    console.error(err)
    alert('서버 오류')
  }
}

watch(email, () => (errors.value.email = ''))
watch(authcode, () => (errors.value.authcode = ''))
watch(userId, () => (errors.value.userId = ''))
watch(userTel, () => (errors.value.userTel = ''))
</script>


<style scoped>
.row {
  gap: 23px 43px;
}
label {
  margin-bottom: 8px;
  font-weight: bold;
}
input[type="text"],
input[type="email"],
input[type="password"] {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.input-error { border: 2px solid red !important; }
.error-text { color: red; font-size: 12px; margin-top: 3px; }
.custom-input::placeholder { font-size: 12px; color: #a3a2a2; font-style: italic; }
</style>
