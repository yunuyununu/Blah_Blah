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

      <div class="row">
        <div class="col" style="text-align: center;">
          &nbsp;&nbsp;&nbsp;<label>변경 할 회사 인증파일</label>&nbsp;&nbsp;
          <input type="file" ref="userFileInput" @change="handleFileChange" accept=".jpg,.jpeg,.png,.pdf"/>
            <p style="margin-top: 5px; color: red;">{{ errors.userFile }}</p>
          <br><br>
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
        alert('회사변경신청이 완료되었습니다.')
        console.log("회사변경 성공 ==",res.data)
        router.push('/')
      }).catch(error => {
        console.error('회사변경 에러:', error)
      })
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
  </style>