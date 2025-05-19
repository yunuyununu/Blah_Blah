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
      <div class="col" style="text-align: center;">
      <label>이메일</label>&nbsp;
      <input type="email" v-model="email" ref="emailInput"
             :class="['custom-input', { 'input-error': errors.email }]" />
      <p v-if="errors.email" class="error-text">{{ errors.email }}</p><br/>

      <button class="btn btn-dark" @click="sendCode" :disabled="isCodeSent">인증코드 전송</button>
    </div></div><br />

    <div v-if="isCodeSent" class="row" style="text-align:center;">
      <label>인증코드</label>&nbsp;
      <input type="text" v-model="code" ref="codeInput"
             :class="['custom-input', { 'input-error': errors.code }]" />
      <p v-if="errors.code" class="error-text">{{ errors.code }}</p><br/>

      <button class="btn btn-dark" @click="verifyCode">코드 확인</button>
    </div>

    <div v-if="isVerified" class="row" style="text-align:center; margin-top: 20px;">
      <button class="btn btn-danger" @click="goReset">비밀번호 재설정</button>
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
const code = ref('')

const emailInput = ref(null)
const codeInput = ref(null)

const isCodeSent = ref(false)
const isVerified = ref(false)

const errors = ref({ email: '', code: '' })

const sendCode = async () => {
  errors.value.email = ''
  if (!email.value) {
    errors.value.email = '이메일을 입력하세요.'
    emailInput.value.focus()
    return
  }

  try {
    const response = await axios.post('http://localhost:80/user/send-reset-code', {
      email: email.value
    })

    if (response.data === 'sent') {
      alert('인증코드가 이메일로 전송되었습니다.')
      isCodeSent.value = true
    } else {
      alert('이메일 전송 실패 또는 등록되지 않은 이메일입니다.')
    }
  } catch (err) {
    console.error(err)
    alert('서버 오류')
  }
}

const verifyCode = async () => {
  errors.value.code = ''
  if (!code.value) {
    errors.value.code = '인증코드를 입력하세요.'
    codeInput.value.focus()
    return
  }

  try {
    const response = await axios.post('http://localhost:80/user/verify-code', {
      email: email.value,
      code: code.value
    })

    if (response.data === 'verified') {
      alert('인증 성공! 비밀번호를 재설정해주세요.')
      isVerified.value = true
    } else {
      errors.value.code = '인증코드가 일치하지 않습니다.'
    }
  } catch (err) {
    console.error(err)
    alert('서버 오류')
  }
}

const goReset = () => {
  router.push({ path: '/reset-password', query: { email: email.value } })
}

watch(email, () => { errors.value.email = '' })
watch(code, () => { errors.value.code = '' })
</script>

<style scoped>
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
