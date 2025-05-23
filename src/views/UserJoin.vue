<template>
  <div class="content">
    <div class="container">
      <v-overlay :model-value="loading" class="d-flex align-center justify-center" persistent>
        <v-progress-circular indeterminate color="primary" size="64" />
      </v-overlay>
      <div class="row">
        <div class="col" style="text-align: center;">
          <h2>회원가입</h2>
          <br>
          <br>
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
           :class="['custom-input', { 'input-error': errors.userId  }]"/>&nbsp;&nbsp;
          <button type="button" class="btn btn-outline-dark" @click="checkUserId">아이디중복확인</button>
          <p v-if="errors.userId" class="error-text">{{ errors.userId }}</p>
          <div v-if="idCheckMessage" style="margin-top: 5px; color: green;">{{ idCheckMessage }}</div>
        </div>
        <div class="col-2"></div></div>
        
        <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>비밀번호</label>
          </div>
          <div class="col-6">
            <input type="password" v-model="userPw" ref="userPwInput" placeholder="8~20자 문자,숫자,특수문자 조합"
            :class="['custom-input', { 'input-error': errors.userPw  }]"/>
            <p v-if="errors.userPw" class="error-text">{{ errors.userPw }}</p>
          </div>
          <div class="col-2"></div>
        </div>

        <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>비밀번호확인</label>
          </div>
          <div class="col-6">
            <input type="password" v-model="pwCheck" ref="pwCheckInput"
            :class="['custom-input', { 'input-error': errors.pwCheck  }]"/>
            <p v-if="errors.pwCheck" class="error-text">{{ errors.pwCheck }}</p>
          </div>
          <div class="col-2"></div>
        </div>

        <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>닉네임</label>
            <br>
          </div>
          <div class="col-6">
            <input type="text" v-model="nickname" ref="nicknameInput" placeholder="공백없이 5~20자"
            :class="['custom-input', { 'input-error': errors.nickname  }]"/>
            <p v-if="errors.nickname" class="error-text">{{ errors.nickname }}</p>
          </div>
          <div class="col-2"></div>
        </div>

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
          <div class="col-2"></div>
          <div class="col-2">
            <label>이메일</label>
          </div>
          <div class="col-6">
            <input type="email" v-model="email" ref="emailInput" placeholder="useremail@example.com"
            :class="['custom-input', { 'input-error': errors.email  }]" :disabled="verifySuccess"/>
            &nbsp;&nbsp;
            <button type="button" class="btn btn-outline-dark" @click="sendAuthCode" :disabled="verifySuccess">인증번호 전송</button>
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
            :class="['custom-input', { 'input-error': errors.authcode  }]" :disabled="verifySuccess"/>&nbsp;&nbsp;
            <button type="button" class="btn btn-outline-dark" @click="authcodeVerify" :disabled="verifySuccess">인증번호확인</button>&nbsp;&nbsp;
            <!-- <button type="button" class="btn btn-dark">재전송</button> -->
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
            <label>회사인증파일</label>
          </div>
          <div class="col-6">
            <input type="file" ref="userFileInput" @change="handleFileChange" accept=".jpg,.jpeg,.png,.pdf"/>
            <p style="margin-top: 5px; color: red;">{{ errors.userFile }}</p>
          </div>
          <div class="col-2">
          </div>
        </div>

        <div class="row">
          <div class="col" style="text-align: center;">
            <br>
            <button type="button" class="btn btn-outline-dark" @click="submit">회원 가입</button>
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


const userId = ref('')
const userPw = ref('')
const pwCheck = ref('')
const nickname = ref('')
const userTel = ref('')
const userFile = ref('')

const handleFileChange = (event) => {
  const file = event.target.files[0]
  userFile.value = file
  console.log("선택된 파일:", file)
}

const userIdInput = ref(null)
const userPwInput = ref(null)
const pwCheckInput = ref(null)
const nicknameInput = ref(null)
const userTelInput = ref(null)
const userFileInput = ref(null)

//이메일관련
const emailInput = ref(null)
const authcodeInput = ref(null)
const email = ref('')
const authcode = ref('')
const verifyMessage = ref('')
const verifySuccess = ref(null)
const countdown = ref(180) // 3분
const timer = ref(null)
const timerVisible = ref(false)
const isEmailChecked = ref(false)
const loading = ref(false)

const errors = ref({
      userId : '',
      userPw : '',
      pwCheck : '',
      nickname : '',
      userTel : '',
      email : '',
      authcode : ''
    })

const isIdChecked = ref(false)
const isIdAvailable = ref(false)
const idCheckMessage = ref('')

const checkUserId = async () => {
  let isValid = true
      
  errors.value = {
    userId : '',
  }  // 초기화
  
  const idPattern = /^[a-zA-Z0-9]{6,12}$/
  
  if (!userId.value) {
    errors.value.userId = '아이디를 입력하세요.'
    if (isValid) userIdInput.value.focus()
    isValid = false
    return
  } else if(!idPattern.test(userId.value)) {
    errors.value.userId = '영문,숫자 공백없이 6~12자리를 입력하세요.'
    if (isValid) userIdInput.value.focus()
    isValid = false
    return
  }

  try {
        const response = await axios.post('http://localhost:80/join/idCheck', {
          userId: userId.value 
          })
          console.log("아이디 중복체크:"+response.data);
          if(response.data === 1) { // 중복
            isIdAvailable.value = false
            isIdChecked.value = true
            errors.value.userId = '중복된 아이디입니다.'
            idCheckMessage.value = false
            console.log('아이디 :'+userId.value)
            if (isValid) userIdInput.value.focus()
            isValid = false
            return
          } else {
            isIdAvailable.value = true
            isIdChecked.value = true
            idCheckMessage.value = '사용 가능한 아이디입니다.'
          }
      } catch (error) {
        console.error('중복 체크 에러:', error)
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

const sendAuthCode = async () => {

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
        alert('인증번호가 이메일로 전송되었습니다.')
      } 
    } catch (error) {
      console.error('이메일 전송 실패:', error)
      loading.value = false
      alert('이메일 전송에 실패했습니다.')
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
        //인증번호 검증되면 타이머 숨기기
        timerVisible.value = false
        verifySuccess.value = true
        isEmailChecked.value = true
        errors.value.authcode = ''
      } else {
        errors.value.authcode = '인증번호가 일치하지 않습니다.'
        authcodeInput.value.focus()
        verifySuccess.value = false
        isEmailChecked.value = false
        return
      }

    } catch (error) {
      console.error('인증번호 검증 실패:', error)
      alert('인증번호 검증에 실패했습니다.')
    }
  }

const submit = () => {

  let isValid = true

  const idPattern = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,12}$/
  const pwPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[\W_]).{8,20}$/
  const nicknamePattern = /^\S{5,20}$/ // 공백 없는 5~20자
  const phonePattern = /^010-\d{4}-\d{4}$/
  const authcodePattern = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6}$/

  // 아이디 체크
  if (!userId.value) {
    errors.value.userId = '아이디를 입력하세요.'
    if (isValid) userIdInput.value.focus()
    return
  } else if (!idPattern.test(userId.value)) {
    errors.value.userId = '영문,숫자 공백없이 6~12자리를 입력하세요'
    if (isValid) userIdInput.value.focus()
    isValid = false
    return
  } else if (!isIdChecked.value) {
    errors.value.userId = '아이디 중복을 확인하세요.'
    if (isValid) userIdInput.value.focus()
    isValid = false
    return
  } else if (idCheckMessage.value === '') {
    errors.value.userId = '아이디를 다시 입력하세요.'
    if (isValid) userIdInput.value.focus()
    isValid = false
    return
  }

  // 비밀번호 체크
  if (!userPw.value) {
    errors.value.userPw = '비밀번호를 입력하세요.'
    if (isValid) userPwInput.value.focus()
    return
  }else if(!pwPattern.test(userPw.value)) {
    errors.value.userPw = '8~20자 문자,숫자,특수문자 조합으로 입력해 주세요.'
    if (isValid) userPwInput.value.focus()
    isValid = false
    return
  } else {
    errors.value.userPw = ''
  }
  if (userPw.value !== pwCheck.value) {
    errors.value.pwCheck = '비밀번호가 불일치힙니다.'
    if (isValid) pwCheckInput.value.focus()
    isValid = false
    return
  } else {
    errors.value.pwCheck = ''
  }

  //닉네임 체크
  if (!nicknamePattern.test(nickname.value)) {
    errors.value.nickname = '닉네임은 공백 없이 5~20자여야 합니다.'
    if (isValid) nicknameInput.value.focus()
    isValid = false
    return
  }

  //전화번호 체크
  if (!userTel.value) {
    errors.value.userTel = '휴대폰 번호 \'-\'제외하고 입력하세요.'
    if (isValid) userTelInput.value.focus()
    isValid = false
    return
  } else if(!phonePattern.test(userTel.value)) {
    errors.value.userTel = '전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)'
    if (isValid) userTelInput.value.focus()
    isValid = false
    return
  } else {
    errors.value.userTel = ''
  }

  //이메일 체크
  if (!email.value) {
    errors.value.email = '이메일 주소를 입력하세요.'
    emailInput.value.focus()
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
  } else if (!isEmailChecked.value) {
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

  // 첨부파일 체크
  if (!userFile.value) {
    errors.value.userFile = '회사 인증 파일을 첨부하세요.'
    if (isValid) userFileInput.value.click() // focus 대신 click으로 파일 선택 창 띄우기
    isValid = false
    return
  }

  axios.post('http://localhost:80/join/emailCheck', { email: email.value })
    .then(res => {
      if (res.data === 1) {
        errors.value.email = '이미 등록된 이메일입니다.'
        emailInput.value.focus()
        errors.value.authcode = ''
        authcode.value = ''
        verifyMessage.value = ''
        isEmailChecked.value = false
        verifySuccess.value = false
        return
      } else { // 이메일 중복 아니면 회원가입 가능

        const formData = new FormData()
        formData.append('userId', userId.value)
        formData.append('userPw', userPw.value)
        formData.append('nickname', nickname.value)
        formData.append('userTel', userTel.value)
        formData.append('email', email.value)
        formData.append('userFile', userFile.value)
      
        axios.post('http://localhost:80/join/userJoin', formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }}).then(response => {
            alert('회원가입이 완료되었습니다.')
            console.log("회원가입 성공 =="+response.data)
            router.push('/login')
          }).catch(error => {
            alert('회원가입에 실패했습니다. 관리자에게 문의하세요.')
            console.error(error)
          })
      }
    }).catch(error => {
      alert('이메일 중복 확인 중 오류가 발생했습니다.');
      console.error(error);
  });
}
  
watch(userId, () => {
    errors.value.userId = ''
    isIdChecked.value = false
    idCheckMessage.value = ''
  })
watch(userPw, () => {
    errors.value.userPw = ''
  })
watch(pwCheck, () => {
  errors.value.pwCheck = ''
})
watch(nickname, () => {
    errors.value.nickname = ''
  })
watch(userTel, () => {
  errors.value.userTel = ''
})
watch(email, () => {
  errors.value.email = ''
})
watch(authcode, () => {
  errors.value.authcode = ''
  verifyMessage.value = ''
  isEmailChecked.value = false
})
watch(userFile, () => {
  errors.value.userFile = ''
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