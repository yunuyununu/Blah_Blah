<template>
  <div class="content">
    <div class="container">

     <div class="row">
        <div class="col" style="text-align: center;">
          <h2>비밀번호 재설정</h2>
          <br><br>
        </div>
      </div>
        <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>비밀번호</label>
          </div>
          <div class="col-6">
            <input type="password" v-model="newPassword" ref="newPwInput"
            placeholder="새 비밀번호를 입력하세요"
            :class="['custom-input', { 'input-error': errors.newPassword  }]"/>
            <p v-if="errors.newPassword" class="error-text">{{ errors.newPassword }}</p>
          </div>
          <div class="col-2"></div>
        </div>

        <div class="row">
          <div class="col-2"></div>
          <div class="col-2">
            <label>비밀번호확인</label>
          </div>
          <div class="col-6">
            <input type="password" v-model="confirmPassword" ref="pwCheckInput"
            placeholder="비밀번호를 다시 입력하세요"
            :class="['custom-input', { 'input-error': errors.confirmPassword  }]"/>
            <p v-if="errors.confirmPassword" class="error-text">{{ errors.confirmPassword }}</p>
          </div>
          <div class="col-2"></div>
        </div>

        <div class="row">
          <div class="col" style="text-align: center;">
            <br>
            <button class="btn btn-primary" @click="resetPassword">변경</button>
            <br>
            <br>
        </div>
        </div>

  </div>
</div>
</template>

<script setup>
import { ref,watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const router = useRouter()

const idx = route.query.u_idx
const id = route.query.u_id

const newPassword = ref('')
const confirmPassword = ref('')

const newPwInput = ref(null)
const pwCheckInput = ref(null)

const errors = ref({
  newPassword: '',
  confirmPassword: ''
})

const validate = () => {
  let isValid = true
  errors.value.newPassword = ''
  errors.value.confirmPassword = ''

  const pwPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[\W_]).{8,20}$/

  if (!newPassword.value){
    errors.value.newPassword = '비밀번호를 입력하세요.'
    if (isValid) newPwInput.value.focus()
    return
  }else if(!pwPattern.test(newPassword.value)) {
    errors.value.newPassword = '8~20자 문자,숫자,특수문자 조합'
    if (isValid) newPwInput.value.focus()
    isValid = false
    return
  } else {
    errors.value.newPassword = ''
  }
  if (newPassword.value !== confirmPassword.value) {
    errors.value.confirmPassword = '비밀번호가 일치하지 않습니다.'
    if (isValid) pwCheckInput.value.focus()
    isValid = false
    return
  } else {
    errors.value.confirmPassword = ''
  }

  return isValid
}

const resetPassword = async () => {
  if (!validate()) return

  const formData = new FormData()
  formData.append('idx', idx)
  formData.append('userId', id)
  formData.append('userPw', newPassword.value)

  try {
    const response = await axios.post('http://localhost:80/login/pwReset', formData)

    console.log("idx==", idx)
    console.log("id==", id)
    console.log("userPw==", newPassword.value)

    console.log("response==", response.data)

    if (response.data === "success") {
      alert('비밀번호가 변경되었습니다.')
      router.push('/login').then(() => {
        window.location.reload()
      })
    } else {
      alert('비밀번호 변경에 실패했습니다.')
    }
  } catch (err) {
    console.error(err)
    alert('서버 오류')
  }
}
watch(newPassword, () => {
    errors.value.newPassword = ''
  })
watch(confirmPassword, () => {
  errors.value.confirmPassword = ''
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
</style>
