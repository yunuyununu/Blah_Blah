<template>
    <div class="content">
    <div class="container" v-if="userInfo.U_C_IDX !== 0">

      <div class="row">
        <div class="col" style="text-align: center;">
          <label>현재 회사</label>&nbsp;&nbsp;
          <input type="text" :value="userInfo.C_NAME" readonly/>
          <br><br>
        </div>
      </div>

      <!-- 회사 인증파일 업로드 -->
 <!-- 회사 인증파일 업로드 - 수평 정렬 -->
<div class="row justify-content-center mb-3">
  <div class="col-md-5 d-flex align-items-center gap-2 flex-wrap">
    <!-- 라벨 -->
    <label class="mb-0" style="white-space: nowrap;">변경 할 회사 인증파일</label>

    <!-- 파일 첨부 버튼 -->
    <label class="file-label mb-0 d-flex align-items-center">
      <span class="upload-button">파일 첨부</span>
      <input type="file" ref="userFileInput" accept=".jpg,.jpeg,.png" @change="handleFileChange" hidden />
    </label>

    <!-- 파일 이름 + 삭제 버튼: 바로 옆에 출력 -->
    <div v-if="userFile" class="d-flex align-items-center gap-1">
      <span class="file-name">{{ userFile.name }}</span>
      <button type="button" class="remove-file" @click="removeFile">×</button>
    </div>
  </div>

  <!-- 에러 메시지 -->
  <div class="col-md-8 mt-2" v-if="errors.userFile">
    <p class="error-text">{{ errors.userFile }}</p>
  </div>
</div>



      <div class="row">
        <div class="col" style="text-align: center;">
          <label>비밀번호확인</label>&nbsp;&nbsp;
          <input type="password" v-model="userPw" ref="userPwInput"
            :class="['custom-input', { 'input-error': errors.userPw  }]" />
            <p v-if="errors.userPw" class="error-text">{{ errors.userPw }}</p>
          <br><br>
        </div>
      </div>

      <div class="row">
        <div class="col" style="text-align: center;">
          <button class="btn btn-outline-dark" @click="submit" >회사 변경 신청</button>
        </div>
      </div>

    </div>
    <div class="container" v-else>

      <div class="row">
        <div class="col" style="text-align: center;">
          <a>현재 관리자가 회사 확인중입니다.</a>
          <br>
          <a>신청한 회사가 확인 되기 전까지는 회사 변경 신청이 불가합니다.</a>
          <br><br>
        </div>
      </div>

    </div>
  </div>
  
  </template>
  
  <script setup>
  import { ref, onMounted, watch } from 'vue';
  import { useRouter } from 'vue-router'
  import axios from 'axios';

  const router = useRouter()

  const userPw = ref('')
  const userFile = ref('')
  const userInfo = ref({})

  const handleFileChange = (event) => {
    const file = event.target.files[0]
    userFile.value = file
    console.log("선택된 파일:", file)
  }

  const userPwInput = ref(null)
  const userFileInput = ref(null)

  const errors = ref({
      userPw : ''
    })

  const fetchMypage = async () => {
    try {
      const res = await axios.get('http://localhost:80/mypage/info');
      userInfo.value = res.data;
      console.log("userInfo=>",userInfo.value);
    } catch (error) {
      console.error('마이페이지 정보 조회 실패', error);
    }
  };

  const submit = () => {

    let isValid = true

    // 첨부파일 체크
    if (!userFile.value) {
      errors.value.userFile = '회사 인증 파일을 첨부하세요.'
      if (isValid) userFileInput.value.click() // focus 대신 click으로 파일 선택 창 띄우기
      isValid = false
      return
    }

    // 비밀번호 체크
    if (!userPw.value) {
      errors.value.userPw = '비밀번호를 입력하세요.'
      if (isValid) userPwInput.value.focus()
      return
    }

    const formData = new FormData()
    formData.append('u_file', userFile.value)

    axios.post('http://localhost:80/mypage/companyChange', formData, {
      headers: {
            'Content-Type': 'multipart/form-data'
      }}).then(res => {
        console.log("회사변경 성공 ==",res.data)
        router.push('/')
      }).catch(error => {
        console.error('회사변경 에러:', error)
      })
  }

  const removeFile = () => {
  userFile.value = ''
  userFileInput.value = ''
}

  onMounted(() => {
    fetchMypage();
  });

  watch(userPw, () => {
    errors.value.userPw = ''
  })
  watch(userFile, () => {
    errors.value.userFile = ''
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
.file-name {
  font-size: 14px;
  color: #333;
  max-width: 150px;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}
  </style>