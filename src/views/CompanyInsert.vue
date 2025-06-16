<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col">
          <form class="company-change-form" @submit.prevent="submitRequest">
            <!-- 회사명 -->
            <div class="form-group">
              <label>회사명 (한글/영문)</label>
              <input type="text" v-model="companyName" ref="companyNameInput" placeholder="30자 이내로 작성하세요."
              :class="['custom-input', { 'input-error': errors.companyName  }]" maxlength="30"/>
              <p v-if="errors.companyName" class="error-text">{{ errors.companyName }}</p>
            </div>

            <!-- 대표 사진 -->
            <div class="form-group">
              <label>회사 대표 사진</label>
              <div class="file-upload-box">
                <label class="file-label">
                  <span class="upload-button">파일 첨부</span>
                  <input type="file" ref="companyFileInput" accept=".jpg,.jpeg,.png" @change="handleFileChange" hidden />
                </label>
                <div v-if="companyFile" class="file-info">
                  <span class="file-name">{{ companyFile.name }}</span>
                  <button type="button" class="remove-file" @click="removeFile">×</button>
                </div>
              </div>
              <p v-if="errors.companyFile" class="error-text">{{ errors.companyFile }}</p>
            </div>

            <!-- 회사소개 -->
            <div class="form-group">
            <label>회사 소개</label>
            <textarea
              v-model="companyIntro"
              ref="companyIntroInput"
              placeholder="200자 이내로 작성하세요."
              :class="['custom-input', { 'input-error': errors.companyIntro }]"
              maxlength="200"
              rows="4"
            ></textarea>
            <p v-if="errors.companyIntro" class="error-text">{{ errors.companyIntro }}</p>
          </div>

            <!-- 설립일자 -->
            <div class="form-group">
              <label>설립일자</label>
              <input type="text" v-model="companyEst" @input="formatEstNumber" ref="companyEstInput" maxlength="10"
              :class="['custom-input', { 'input-error': errors.companyEst  }]" placeholder="숫자만 입력하세요. (8자리) ex. 2024-01-01"/>
              <p v-if="errors.companyEst" class="error-text">{{ errors.companyEst }}</p>
            </div>

            <!-- 사업자등록번호 -->
            <div class="form-group">
              <label>사업자등록번호</label>
              <input type="text" v-model="companyBusiness" @input="formatBusinessNumber" ref="companyBusinessInput" maxlength="12"
              :class="['custom-input', { 'input-error': errors.companyBusiness  }]" placeholder="숫자만 입력하세요. (10자리) ex. 123-45-67890"/>
              <p v-if="errors.companyBusiness" class="error-text">{{ errors.companyBusiness }}</p>
            </div>
            <br>
            <div style="text-align: center;">
              <button type="button" class="submit-btn" @click="submit">신청하기</button>
            </div>
          </form>
          <div class="no-result-text" style="text-align: center;">
              * 회사 목록 조회 후 없을 경우에만 신청하시기 바랍니다. 
            </div>
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useToast } from 'vue-toastification'
import { useUserStore } from '@/store/userStore'

const userStore = useUserStore()

const router = useRouter()

const toast = useToast()
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

  const date = companyEst.value.replace(/-/g, '');

  if (!companyEst.value) {
    errors.value.companyEst = '설립일자를 입력하세요.'
    if (isValid) companyEstInput.value.focus()
    return
  } else if (!/^\d{8}$/.test(date)) {
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
        console.log("회사신청 성공 ==",response.data)
        toast.success('회사신청이 완료되었습니다.')
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

const formatEstNumber = () => {
  let digits = companyEst.value.replace(/\D/g, '') // 숫자만 추출

  // 최대 8자리까지만 허용 (yyyyMMdd)
  if (digits.length > 9) {
    digits = digits.slice(0, 9)
  }

  if (digits.length <= 4) {
    companyEst.value = digits
  } else if (digits.length <= 6) {
    companyEst.value = `${digits.slice(0, 4)}-${digits.slice(4)}`
  } else {
    companyEst.value = `${digits.slice(0, 4)}-${digits.slice(4, 6)}-${digits.slice(6)}`
  }
}


const formatBusinessNumber = () => {
  let digits = companyBusiness.value.replace(/\D/g, ''); // 숫자만 추출

  if (digits.length > 10) {
    digits = digits.slice(0, 10); // 최대 10자리까지
  }

  if (digits.length <= 3) {
    companyBusiness.value = digits;
  } else if (digits.length <= 5) {
    companyBusiness.value = `${digits.slice(0, 3)}-${digits.slice(3)}`;
  } else {
    companyBusiness.value = `${digits.slice(0, 3)}-${digits.slice(3, 5)}-${digits.slice(5)}`;
  }
}

const removeFile = () => {
  companyFile.value = ''
  companyFileInput.value = ''
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
input[type="file"],
textarea {
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-family: inherit;
  font-size: 14px;
  resize: none; /* textarea 사이즈 수동 변경 막기 (필요 시 제거) */
  width: 100%;
  box-sizing: border-box;
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
.no-result-text {
  color: gray;
}
.file-upload-box {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.upload-button {
  display: inline-block;
  background-color: #c0e0fa;
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

</style>

