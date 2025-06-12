<template>
  <div class="content">
    <div class="container">
      <v-overlay :model-value="loading" class="d-flex align-center justify-center" persistent>
        <v-progress-circular indeterminate color="primary" size="64" />
      </v-overlay>
     <div class="row">
        <div class="col" style="text-align: center;">
          <h2>비밀번호 찾기</h2>
          <br><br>
        </div>
      </div>
      <div class="row">
        <div class="col-2"></div>
        <div class="col-2">
          <label>아이디</label>
          <br>
        </div>
        <div class="col-6" style="gap: 5px;">
          <input type="text"  v-model="userId" ref="userIdInput" placeholder="영문,숫자 공백없이 6~12자리를 입력하세요." :disabled="changeId"
           :class="['custom-input', { 'input-error': errors.userId  }]"/>
          <p v-if="errors.userId" class="error-text">{{ errors.userId }}</p>
          <div v-if="idCheckMessage" style="margin-top: 5px; color: green;">{{ idCheckMessage }}</div>
        </div>
        <div class="col-2"></div></div>

      <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>이메일</label>
          </div>
          <div class="col-6">
            <input type="email" v-model="email" ref="emailInput" placeholder="useremail@example.com" :disabled="changeEmail || emailDisabled"
            :class="['custom-input', { 'input-error': errors.email  }]"/>
            &nbsp;&nbsp;
            <button type="button" class="btn btn-outline-dark" @click="sendAuthCode" :disabled="emailBtnDisabled" >인증번호 전송</button>
            <button type="button" class="btn btn-outline-dark" @click="sendAuthCode" :disabled="emailAgainDisabled" v-if="timerVisible === false && emailAgain === true">재전송</button>&nbsp;&nbsp;
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
            <input type="text" v-model="authcode" ref="authcodeInput" placeholder="인증코드 6자리" :disabled="authcodeDisabled"
            :class="['custom-input', { 'input-error': errors.authcode  }]"/>&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-dark" @click="authcodeVerify">인증번호확인</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-dark" @click="emailReset" v-if="!changeId">초기화</button>
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
        <!-- <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>전화번호</label>
          </div>
          <div class="col-6">
            <input type="text" v-model="userTel"
            ref="userTelInput"
            placeholder="숫자만 입력하세요."
            @input="formatPhoneNumber"
            :disabled="changeTel"
            :class="['custom-input', { 'input-error': errors.userTel }]"/>
            <p v-if="errors.userTel" class="error-text">{{ errors.userTel }}</p>
          </div>
          <div class="col-2"></div>
        </div> -->

        <div class="row">
          <div class="col" style="text-align: center;">
            <br>
            <button :disabled="!isCodeVerified" class="btn btn-danger" @click="resetPassword">비밀번호 재설정</button>
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
import { useRoute } from 'vue-router';
import { useToast } from 'vue-toastification'

import axios from 'axios'

const router = useRouter()
const toast = useToast()

const email = ref('')
const authcode = ref('')
const userId = ref('')
//const userTel = ref('')

const emailInput = ref(null)
const authcodeInput = ref(null)
const userIdInput = ref(null)
//const userTelInput = ref(null)

const emailDisabled = ref(false)
const authcodeDisabled = ref(false)
const emailBtnDisabled = ref(false)
const emailAgainDisabled = ref(false)

const verifyMessage = ref('')
const verifySuccess = ref(null)
const emailAgain = ref(false)

const countdown = ref(180)
const timer = ref(null)
const timerVisible = ref(false)

const route = useRoute();
const changeId = route.query.userId;
//const changeTel = route.query.userTel;
const changeEmail = route.query.userEmail;

if(changeId && changeEmail) {
  userId.value = changeId;
  //userTel.value = changeTel;
  email.value = changeEmail;
}

const loading = ref(false)

const errors = ref({
  email: '',
  authcode: '',
  userId: ''
})

//const idCheckMessage = ref('')
const isCodeVerified = ref(false)

const sendAuthCode = async () => {
       authcode.value = ''
    verifyMessage.value = ''
    verifySuccess.value = null
  let isValid = true

  errors.value.email = ''

  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

  if (!email.value) {
    errors.value.email = '이메일을 입력하세요.'
    emailInput.value.focus()
    isValid = false
    return
  }else if (!emailPattern.test(email.value)) {
      errors.value.email = '이메일 형식에 맞게 입력하세요.'
      emailInput.value.focus()
      isValid = false
      return
  }

  try {
    loading.value = true

    const response = await axios.post('http://localhost:80/join/pwEmailsend', {
      email: email.value
    })

    if (response.data === 'success') {
      loading.value = false
      startTimer()
      toast.success('이메일 전송이 완료되었습니다.')
      emailDisabled.value = true
      if(emailAgain.value === true) {
          emailAgainDisabled.value = true
        }else {
          emailBtnDisabled.value = true
        }
    } else {
      loading.value = false
      errors.value.email = '등록되지 않은 이메일입니다.'
      emailInput.value.focus()
      return
    }
  } catch (err) {
    console.error(err)
    loading.value = false
    alert('서버 오류')
  }
  return isValid
}

const startTimer = () => {
  timerVisible.value = true
  countdown.value = 180
  clearInterval(timer?.value)
  timer.value = setInterval(() => {
    if (countdown.value > 0) {
        countdown.value--
      } else {
        clearInterval(timer.value)
        timerVisible.value = false
      }
  }, 1000)
}

const formatTime = () => {
    const minutes = String(Math.floor(countdown.value / 60)).padStart(2, '0')
    const seconds = String(countdown.value % 60).padStart(2, '0')
    return `${minutes}:${seconds}`
  }

// const formatPhoneNumber = () => {
//   let digits = userTel.value.replace(/\D/g, '') // 숫자만 추출

//   // 최대 11자리까지만 허용
//   if (digits.length > 11) {
//     digits = digits.slice(0, 11)
//   }

//   if (digits.length < 4) {
//     userTel.value = digits
//   } else if (digits.length < 8) {
//     userTel.value = `${digits.slice(0, 3)}-${digits.slice(3)}`
//   } else {
//     userTel.value = `${digits.slice(0, 3)}-${digits.slice(3, 7)}-${digits.slice(7)}`
//   }
// }

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
      verifyMessage.value = '이메일 인증에 성공했습니다.'
      timerVisible.value = false
      clearInterval(timer.value)
      verifySuccess.value = true
      isCodeVerified.value = true
      authcodeDisabled.value = true
    } else {
      verifyMessage.value = '인증번호가 올바르지 않습니다.'
      verifySuccess.value = false
    }
  } catch (err) {
    console.error(err)
    alert('서버 오류')
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
  // if (!userTel.value) {
  //   errors.value.userTel = '전화번호를 입력하세요.'
  //   userTelInput.value.focus()
  //   return
  // }

  try {
    const response = await axios.post('http://localhost:80/login/searchPw', {
      email: email.value,
      userId: userId.value
    })
    
    if (response.data) {
      router.push({ path: '/passwdreset', query: { u_idx: response.data.u_idx, u_id: response.data.u_id } })

    } else {
      alert('일치하는 회원 정보가 없습니다.')
    }
  } catch (err) {
    console.error(err)
    alert('서버 오류')
  }
}

const emailReset = () => {
  emailInput.value = null
 authcodeInput.value = null
 email.value = ''
 authcode.value = ''
 verifyMessage.value = ''
 verifySuccess.value = null
 countdown.value = ''
 timer.value = null
 timerVisible.value = false
 loading.value = false
 clearInterval(timer.value)

 emailDisabled.value = false
 authcodeDisabled.value = false
 emailAgain.value = false

emailBtnDisabled.value = false
emailAgainDisabled.value = false
}

watch(email, () => (errors.value.email = ''))
watch(authcode, () => {
  errors.value.authcode = ''
  verifyMessage.value = ''
})
watch(userId, () => (errors.value.userId = ''))
watch(countdown, (newVal) => {
  if (newVal === 0) {
    clearInterval(timer.value)
    verifyMessage.value = '시간이 초과되었습니다. 인증번호를 재전송 해주세요.'
    verifySuccess.value = false
    authcodeDisabled.value = false
    timerVisible.value = false
    errors.value.authcode = ''
    emailAgain.value = true
  }
})
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
input[disabled] {
  background-color: #e9ecef;
  color: #6c757d;
  cursor: not-allowed;
}
</style>
