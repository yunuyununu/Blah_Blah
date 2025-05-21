<template>
  <div class="content">
    <div class="container">

      <div class="row">
        <div class="col" style="text-align: center;">
          <h2>아이디 찾기</h2>
          <br><br>
        </div>
      </div>

      <div class="row">
        <div class="col" style="text-align: center;">
          <label>&nbsp;&nbsp;이메일</label>&nbsp;&nbsp;
          <input type="email" v-model="email" ref="emailInput" placeholder="useremail@example.com"
                 :class="['custom-input', { 'input-error': errors.email }]" />
          <p v-if="errors.email" class="error-text">{{ errors.email }}</p>
          <br><br>
        </div>
      </div>

      <div class="row">
        <div class="col" style="text-align: center;">
          <label>전화번호</label>&nbsp;&nbsp;
          <input type="text" v-model="userTel" ref="userTelInput" placeholder="숫자만 입력하세요."
          @input="formatPhoneNumber"
          :class="['custom-input', { 'input-error': errors.userTel }]" />
          <p v-if="errors.userTel" class="error-text">{{ errors.userTel }}</p>
          <br><br>
        </div>
      </div>

      <div class="row">
        <div class="col" style="text-align: center;">
          <button type="button" class="btn btn-dark" @click="submit">아이디 찾기</button>
          <br><br>
        </div>
      </div>

      <div class="row" v-if="foundId">
        <div class="col" style="text-align: center;">
          <p><strong>{{ foundId }}</strong></p>
        </div>
      </div>

      <div class="row">
        <div class="col" style="text-align: center;">
          <button class="btn btn-light" @click="goLogin">로그인 페이지로 이동</button>
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
const emailInput = ref(null)
const foundId = ref('')

const userTel = ref('')
const userTelInput = ref(null)

const errors = ref({
  email: ''
})

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

const submit = async () => {
  errors.value.email = ''
  foundId.value = ''

  if (!email.value) {
    errors.value.email = '이메일을 입력하세요.'
    emailInput.value.focus()
    return
  }
  if (!userTel.value) {
    errors.value.userTel = '전화번호를 입력하세요.'
    userTelInput.value.focus()
    return
  }

  try {
    const response = await axios.post('http://localhost:80/login/searchId', {
      email: email.value,
      userTel: userTel.value
    })

    if (response.data) {
      foundId.value = '회원님의 아이디는 "'+response.data.u_id+'" 입니다.'
    } else {
      foundId.value = '해당 이메일/전화번호로 등록된 아이디가 없습니다.';
    }

  } catch (error) {
    console.error('아이디 찾기 오류:', error)
    alert('아이디 찾기 중 오류가 발생했습니다.')
  }
}

const goLogin = () => {
  router.push('/login')
}

watch(email, () => {
  errors.value.email = ''
})
watch(userTel, () => {
  errors.value.userTel = ''
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
</style>
