<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col">
          <form class="company-change-form" @submit.prevent="submitRequest">
            <!-- 회사명 -->
            <div class="form-group">
              <label>회사명 (한글/영문)</label>
              <input type="text" v-model="companyName" ref="companyNameInput"
              :class="['custom-input', { 'input-error': errors.companyName  }]" />
              <p v-if="errors.companyName" class="error-text">{{ errors.companyName }}</p>
            </div>

            <!-- 대표 사진 -->
            <div class="form-group">
              <label>회사 대표 사진</label>
              <input type="file" ref="companyFileInput" accept=".jpg,.jpeg,.png,.pdf" @change="handleFileChange" />
              <p style="margin-top: 5px; color: red;">{{ errors.companyFile }}</p>
            </div>

            <!-- 회사소개 -->
            <div class="form-group">
              <label>회사 소개</label>
              <input type="text" v-model="companyIntro" ref="companyIntroInput"
              :class="['custom-input', { 'input-error': errors.companyIntro  }]" />
              <p v-if="errors.companyIntro" class="error-text">{{ errors.companyIntro }}</p>
            </div>

            <!-- 설립일자 -->
            <div class="form-group">
              <label>설립일자 (8자리, 예: 20240101)</label>
              <input type="text" v-model="companyEst" ref="companyEstInput" maxlength="8"
              :class="['custom-input', { 'input-error': errors.companyEst  }]" />
              <p v-if="errors.companyEst" class="error-text">{{ errors.companyEst }}</p>
            </div>

            <!-- 사업자등록번호 -->
            <div class="form-group">
              <label>사업자등록번호 (10자리, 예: 1234567890)</label>
              <input type="text" v-model="companyBusiness" ref="companyBusinessInput" maxlength="10"
              :class="['custom-input', { 'input-error': errors.companyBusiness  }]"/>
              <p v-if="errors.companyBusiness" class="error-text">{{ errors.companyBusiness }}</p>
            </div>
            <br>
            <div style="text-align: center;">
              <button type="button" class="submit-btn" @click="submit">신청하기</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

import { useUserStore } from '@/store/userStore'

const userStore = useUserStore()

const router = useRouter()

const companyName = ref('')
const companyIntro = ref('')
const companyEst = ref('')
const companyBusiness = ref('')
const companyFile = ref('')

const handleFileChange = (event) => {
  const file = event.target.files[0]
  companyFile.value = file
  console.log("선택된 파일:", file)
}

const companyNameInput = ref(null)
const companyIntroInput = ref(null)
const companyEstInput = ref(null)
const companyBusinessInput = ref(null)
const companyFileInput = ref(null)

const errors = ref({
      companyName : '',
      companyIntro : '',
      companyEst : '',
      companyBusiness : ''
    })

const submit = () => {

  let isValid = true

  if (!companyName.value) {
    errors.value.companyName = '회사명을 입력하세요.'
    if (isValid) companyNameInput.value.focus()
    return
  }
  if (!companyFile.value) {
    errors.value.companyFile = '회사 대표 사진을 첨부하세요.'
    if (isValid) companyFileInput.value.click() // focus 대신 click으로 파일 선택 창 띄우기
    isValid = false
    return
  }
  if (!companyIntro.value) {
    errors.value.companyIntro = '회사 소개를 입력하세요.'
    if (isValid) companyIntroInput.value.focus()
    return
  }
  if (!companyEst.value) {
    errors.value.companyEst = '설립일자를 입력하세요.'
    if (isValid) companyEstInput.value.focus()
    return
  } else if (companyEst.value.length !== 8 || isNaN(companyEst.value)) {
    alert('설립일자는 숫자 8자리로 입력해주세요.');
    return;
  }
  if (!companyBusiness.value) {
    errors.value.companyBusiness = '사업자등록번호를 입력하세요.'
    if (isValid) companyBusinessInput.value.focus()
    return
  }
  

  const formData = new FormData()
        formData.append('cr_name', companyName.value)
        formData.append('cr_intro', companyIntro.value)
        formData.append('cr_est', companyEst.value)
        formData.append('cr_business', companyBusiness.value)
        formData.append('cr_logo', companyFile.value)
      
  if(userStore.isLogin == true) {
    axios.post('http://localhost:80/company/companyInsert', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }}).then(response => {
        alert('회사신청이 완료되었습니다.')
        console.log("회사신청 성공 ==",response.data)
        router.push('/')
      }).catch(error => {
        alert('회사신청에 실패했습니다. 관리자에게 문의하세요.')
        console.error(error)
      })
  } else {
    alert('로그인 후 이용해주세요.')
    return
  }
        

}

watch(companyName, () => {
    errors.value.companyName = ''
  })
watch(companyIntro, () => {
    errors.value.companyIntro = ''
  })
watch(companyEst, () => {
  errors.value.companyEst = ''
})
watch(companyBusiness, () => {
    errors.value.companyBusiness = ''
  })
watch(companyFile, () => {
    errors.value.companyFile = ''
  })
</script>


  
<style scoped>
.company-change-form {
  max-width: 500px;
  margin: auto;
  padding: 24px;
  border: 1px solid #ccc;
  border-radius: 8px;
}
.form-group {
  margin-bottom: 16px;
  display: flex;
  flex-direction: column;
}
label {
  font-weight: bold;
  margin-bottom: 6px;
}
input[type="text"],
input[type="number"],
input[type="file"] {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.submit-btn {
  background-color: black;
  color: white;
  padding: 10px;
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

