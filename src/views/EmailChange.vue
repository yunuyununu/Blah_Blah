<template>
    <div class="content">
    <div class="container">
      <v-overlay :model-value="loading" class="d-flex align-center justify-center" persistent>
        <v-progress-circular indeterminate color="primary" size="64" />
      </v-overlay>
 <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>이메일</label>
          </div>
          <div class="col-6">
            <input type="email" v-model="email" ref="emailInput" placeholder="useremail@example.com"
            :class="['custom-input', { 'input-error': errors.email  }]" :disabled="emailDisabled" maxlength="50"/>
            &nbsp;&nbsp;
            <button type="button" class="btn btn-outline-dark" @click="EmailCheck" v-if="emailCheck === false">인증번호전송</button>
            <button type="button" class="btn btn-outline-dark" @click="sendAuthCode" :disabled="emailAgainDisabled" v-if="timerVisible === false && emailCheck === true && emailAgain === true">재전송</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-dark" @click="sendAuthCode" :disabled="emailBtnDisabled" v-if="emailCheck === true">인증번호 전송</button>
            <p v-if="errors.email" class="error-text">{{ errors.email }}</p>
            <div v-if="verifyEmail" style="margin-top: 3px;">
                <p :style="{ color: verifyOK === true ? 'green' : verifyOK === false ? 'red' : 'black' }">
                  {{ verifyEmail }}
                </p>
            </div>
          </div>
          <div class="col-2"></div>
        </div>

        <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>인증번호</label>
          </div>
          <div class="col-6">
            <input type="text" v-model="authcode" ref="authcodeInput" placeholder="인증코드 6자리" maxlength="6"
            :class="['custom-input', { 'input-error': errors.authcode  }]" :disabled="authcodeDisabled"/>&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-dark" @click="authcodeVerify" :disabled="authcodeBtnDisabled">인증번호확인</button>&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-dark" @click="emailReset">초기화</button>
            <p v-if="errors.authcode" class="error-text">{{ errors.authcode }}</p>
            <!-- 타이머 -->
            <div v-if="timerVisible" style="margin-top: 3px;">
                <p style="font-weight: bold;">남은 시간: {{ formatTime() }}</p>
            </div>
    
            <!-- 인증 결과 메시지 -->
            <div v-if="verifyMessage" style="margin-top: 3px;">
                <p :style="{ color: verifySuccess === true ? 'green' : verifySuccess === false ? 'red' : 'black' }">
                  {{ verifyMessage }}
                </p>
            </div>
          </div>
          <div class="col-2">
          </div>
        </div>

      <div class="row">
        <div class="col" style="text-align: center;">
          <button class="btn btn-outline-dark" @click="submit" >이메일 변경</button>
        </div>
      </div>

    </div>
  </div>
  
  </template>
  
  <script setup>
  import { ref, watch } from 'vue';
  import { useRouter } from 'vue-router'
  import axios from 'axios';
  import { useToast } from 'vue-toastification'

const toast = useToast()
  const router = useRouter()
const emailInput = ref(null)
const authcodeInput = ref(null)
const email = ref('')
const authcode = ref('')
const verifyMessage = ref('')
const verifySuccess = ref(null)
const verifyEmail = ref('')
const verifyOK = ref('')
const countdown = ref(180) // 3분
const timer = ref(null)
const timerVisible = ref(false)
const isEmailChecked = ref(false)
const isAuthcodeChecked = ref(false)
const loading = ref(false)
const emailAgain = ref(false)

const authcodeBtnDisabled = ref(false)
const emailDisabled = ref(false)
const emailBtnDisabled = ref(false)
const authcodeDisabled = ref(false)
const emailAgainDisabled = ref(false)
const emailCheck = ref(false)

const errors = ref({
      email : '',
      authcode : ''
    })

const EmailCheck = async () => {
  authcode.value = ''
  verifyMessage.value = ''
  verifySuccess.value = null
  errors.value = { email: '' }

  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

  if (!email.value) {
    errors.value.email = '이메일 주소를 입력하세요.'
    emailInput.value.focus()
    return
  }

  if (!emailPattern.test(email.value)) {
    errors.value.email = '이메일 형식에 맞게 입력하세요.'
    emailInput.value.focus()
    return
  }

  try {
    // 이메일 중복 확인
    const res = await axios.post('http://localhost:80/join/emailCheck', { email: email.value })
    if (res.data === 1) {
      errors.value.email = '이미 등록된 이메일입니다.'
      emailInput.value.focus()
      authcode.value = ''
      verifyMessage.value = ''
      isEmailChecked.value = false
      verifySuccess.value = false
      emailDisabled.value = false
      return
    }

    // 이메일 전송
    loading.value = true
    const response = await axios.post('http://localhost:80/join/emailsend', { email: email.value })

    if (response.data === 'success') {
      startTimer()
      emailDisabled.value = true

      if (emailAgain.value === true) {
        emailAgainDisabled.value = true
      } else {
        emailBtnDisabled.value = true
      }
      loading.value = false
      toast.success('이메일 전송이 완료되었습니다.')
      emailCheck.value = true
      verifyEmail.value = '이메일 중복 확인 후 인증번호 전송이 완료되었습니다.'
      verifyOK.value = true
      isEmailChecked.value = true
    } else {
      toast.error('이메일 전송에 실패했습니다.')
    }
  } catch (error) {
    console.error('에러:', error)
    alert('이메일 처리 중 오류가 발생했습니다. 다시 시도해주세요.')
    loading.value = false
  }
}
    const sendAuthCode = async () => {
     authcode.value = ''
    verifyMessage.value = ''
    verifySuccess.value = null
    let isValid = true

    errors.value = {
      email    : ''
     }  // 초기화

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/

    if (!email.value) {
      errors.value.email = '이메일 주소를 입력하세요.'
      emailInput.value.focus()
      isValid = false
      return
    } else if (!emailPattern.test(email.value)) {
      errors.value.email = '이메일 형식에 맞게 입력하세요.'
      emailInput.value.focus()
      isValid = false
      return
    }

    try {
      loading.value = true
      
      const response = await axios.post('http://localhost:80/join/emailsend', {
        email: email.value
      })
      console.log('응답:', response.data)
      if (response.data === 'success') {
        loading.value = false
        startTimer() // 인증번호 검증 시간 측정
        emailDisabled.value = true
        
        if(emailAgain.value === true) {
          emailAgainDisabled.value = true
        }else {
          emailBtnDisabled.value = true
        }

        toast.success('이메일 전송이 완료되었습니다.')
      } 
    } catch (error) {
      console.error('이메일 전송 실패:', error)
      loading.value = false
      alert('이메일 전송에 실패했습니다. 관리자에게 문의하거나 다시 시도해보세요.')
    }

    return isValid

  }

  const startTimer = () => {
  timerVisible.value = true
  countdown.value = 180 // 3분
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

  // 타이머 형태 (분:초)
  const formatTime = () => {
    const minutes = String(Math.floor(countdown.value / 60)).padStart(2, '0')
    const seconds = String(countdown.value % 60).padStart(2, '0')
    return `${minutes}:${seconds}`
  }

  const authcodeVerify = async () => {

    let isValid = true

    if (!authcode.value) {
        errors.value.authcode = '인증번호를 입력하세요.'
        if (isValid) authcodeInput.value.focus()
        isValid = false
        return
    }

    try {
      const response = await axios.post('http://localhost:80/join/authCodeVerify', {
        email : email.value,
        authcode: authcode.value
      })
      console.log('응답:', response.data)

      if (response.data === 'success') {
        verifyMessage.value = '인증번호가 확인되었습니다.'
        verifySuccess.value = true
        //인증번호 검증되면 타이머 숨기기
        timerVisible.value = false
        clearInterval(timer.value)
        authcodeBtnDisabled.value = true
        isAuthcodeChecked.value = true
        authcodeDisabled.value = true
        errors.value.authcode = ''
      } else {
        errors.value.authcode = '인증번호가 일치하지 않습니다.'
        authcodeInput.value.focus()
        verifySuccess.value = false
        isAuthcodeChecked.value = false
        return
      }

    } catch (error) {
      console.error('인증번호 검증 실패:', error)
      alert('인증번호 검증에 실패했습니다.')
    }
  }

  const submit = () => {

  let isValid = true

  const authcodePattern = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6}$/


  //이메일 체크
  if (!email.value) {
    errors.value.email = '이메일 주소를 입력하세요.'
    emailInput.value.focus()
    isValid = false
    return
  } else if (!isEmailChecked.value) {
    errors.value.email = '이메일 중복을 확인하세요.'
    if (isValid) isEmailChecked.value.focus()
    isValid = false
    return
  }

  //인증번호 체크
  if (!authcode.value) {
    errors.value.authcode = '인증번호를 입력하세요.'
    if (isValid) authcodeInput.value.focus()
    isValid = false
    return
  } else if(!authcodePattern.test(authcode.value)) {
    errors.value.authcode = '인증코드 6자리를 입력하세요.'
    if (isValid) authcodeInput.value.focus()
    isValid = false
    return
  } else if (!isAuthcodeChecked.value) {
    errors.value.authcode = '인증번호를 검증하세요.'
    if (isValid) authcodeInput.value.focus()
    isValid = false
    return
  } else if (verifyMessage.value === '') {
    errors.value.authcode = '인증번호를 다시 입력하세요.'
    if (isValid) authcodeInput.value.focus()
    isValid = false
    return
  }

  // 이메일 변경 폼
    const formData = new FormData()
    formData.append('u_email', email.value)
  
    axios.post('http://localhost:80/mypage/emailChange', formData)
    .then(response => {
      if (response.data === 'fail') {
        alert('이메일변경에 실패했습니다. 관리자에게 문의하세요.')
        console.error('이메일변경 실패:', response.data)
        return
      }
      router.push('/mypage/myinformation')
    })
    .catch(error => {
      console.error(error)
    })

}

const emailReset = () => {
  emailInput.value = null
 authcodeInput.value = null
 email.value = ''
 authcode.value = ''
 verifyMessage.value = ''
 verifySuccess.value = null
 verifyEmail.value = ''
 verifyOK.value = ''
 countdown.value = ''
 timer.value = null
 timerVisible.value = false
 clearInterval(timer.value)
 isEmailChecked.value = false
 isAuthcodeChecked.value = false
 loading.value = false

 authcodeBtnDisabled.value = false
 emailDisabled.value = false
 emailBtnDisabled.value = false
 authcodeDisabled.value = false
 emailCheck.value = false
 emailAgain.value = false
}
watch(email, () => {
  errors.value.email = ''
})
watch(authcode, () => {
  errors.value.authcode = ''
  verifyMessage.value = ''
})
watch(countdown, (newVal) => {
  if (newVal === 0) {
    clearInterval(timer.value)
    verifyMessage.value = '시간이 초과되었습니다. 인증번호를 재전송 해주세요.'
    verifySuccess.value = false
    verifyEmail.value = false
    authcodeBtnDisabled.value = false
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

.input-error {
  border: 2px solid red !important;
}

.error-text {
  color: red;
  font-size: 12px;
  margin-top: 3px;
  margin-bottom: 8px;
}

.custom-input::placeholder {
  font-size: 12px;
  color: #a3a2a2;
  font-style: italic;
}
.file-upload-box {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.upload-button {
  display: inline-block;
  background-color: #f0f0f0;
  border: 1px solid #ccc;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #333;
}

.file-name {
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.remove-file {
  background: none;
  border: none;
  color: red;
  font-size: 18px;
  line-height: 1;
  cursor: pointer;
}
.file-label {
  display: inline-block;
  cursor: pointer;
}

.upload-button {
  background-color: #c0e0fa;
  border: 1px solid #ccc;
  padding: 6px 14px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  display: inline-block;
}

.file-info {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  font-size: 13px;
  margin-top: 8px;
}

.file-name {
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 14px;
}

.remove-file {
  background: none;
  border: none;
  color: red;
  font-size: 18px;
  cursor: pointer;
}

.error-text {
  font-size: 12px;
  color: red;
}

  </style>