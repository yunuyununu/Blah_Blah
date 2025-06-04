<template>

    <main class="main-content">
      <div class="form-grid">
        <div class="form-group">
          <label>아이디</label>
          <input type="text" v-model="userInfo.U_ID" readonly />
        </div>

        <div class="form-group">
          <label>비밀번호</label>
          <button type="button" class="change-password-btn" @click="goPwChange">비밀번호 변경</button>
        </div>

        <div class="form-group">
          <label>닉네임</label>
          <div v-if="!isEditingNickname" class="edit-view">
            <input type="text" v-model="userInfo.U_NICNAME" readonly />
            <button class="change-password-btn" @click="startNicknameEdit">수정</button>
          </div>
          <div v-else class="edit-view">
            <input
              type="text"
              v-model="newNickname"
              ref="nicknameInput"
              placeholder="변경할 닉네임을 입력해주세요"
              :class="['custom-input', { 'input-error': errors.newNickname  }]"
            />
            <button class="change-password-btn" @click="updateNickname">수정완료</button>
            <button class="change-password-btn" @click="cancelNicknameEdit">취소</button>
          </div>
          <p v-if="errors.newNickname" class="error-text">{{ errors.newNickname }}</p>
        </div>

        <div class="form-group">
          <label>이메일</label>
          <input type="text" v-model="userInfo.U_EMAIL" readonly/>
        </div>

        <div class="form-group">
          <label>전화번호</label>
            <div v-if="!isEditingPhone" class="edit-view">
              <input type="text" v-model="userTel" readonly/>
              <button class="change-password-btn" @click="startPhoneEdit">수정</button>
            </div>
          <!-- 전화번호 수정 입력창 -->
            <div v-else class="edit-view">
              <input
                type="text"
                v-model="newPhone"
                ref="newPhoneInput"
                placeholder="변경할 휴대폰번호를 입력해주세요"
                :class="['custom-input', { 'input-error': errors.newPhone }]"
              />
              <button class="change-password-btn" @click="requestPhoneVerification">수정완료</button>
              <button class="change-password-btn" @click="cancelPhoneEdit">취소</button>
            </div>
            <p v-if="errors.newPhone" class="error-text">{{ errors.newPhone }}</p>
        </div>
        
        <div class="form-group" v-if="userInfo.U_C_IDX === 0">
          <label>회원회사</label>
          <input type="text" placeholder="관리자 확인중입니다." readonly />
        </div>

        <div class="form-group" v-else>
          <label>회원회사</label>
          <input type="text" :value="userInfo.C_NAME" readonly />
        </div>

        <div class="form-group company-change" v-if="userInfo.U_STATUS == 'Uncertified'">
          <label>회사인증유무</label>
          <input type="text" placeholder="관리자 확인중입니다." readonly/>
        </div>
        <div class="form-group company-change" v-if="userInfo.U_STATUS == 'Certified'">
          <label>회사인증유무</label>
          <input type="text" :value="'Y'" readonly/>
        </div>
        <div class="form-group company-change" v-if="userInfo.U_REVIEW == 'N'">
          <label>리뷰작성유무</label>
          <input type="text" :value="'N'" readonly/>
        </div>
        <div class="form-group company-change" v-if="userInfo.U_REVIEW == 'Y'">
          <label>리뷰작성유무</label>
          <input type="text" :value="'Y'" readonly/>
        </div>
        <div class="form-group">
          <label>회원가입일자</label>
          <input type="text" v-model="userInfo.U_JOINDATE" readonly/>
        </div>
      </div>

      <div class="submit-btn-wrapper">
        <button class="btn btn-danger" @click="userWithdraw">회원 탈퇴</button>
      </div>
    </main>
  
  </template>
  
  <script setup>
  import { ref, onMounted,watch  } from 'vue';
  import { useRouter } from 'vue-router'
  import axios from 'axios';

  const router = useRouter()

  const userInfo = ref({});
  const userId = ref('');
  const userTel = ref('');
  const userEmail = ref('');

  const errors = ref({
      newNickname : '',
      newPhone : ''
    })

  const fetchMypage = async () => {
    try {
      const res = await axios.get('http://localhost:80/mypage/info');
      userInfo.value = res.data;
      userId.value = userInfo.value.U_ID;
      userTel.value = formatPhone(userInfo.value.U_PHONE);
      userEmail.value = userInfo.value.U_EMAIL;

    } catch (error) {
      console.error('마이페이지 정보 조회 실패', error);
    }
  };

  onMounted(() => {
    fetchMypage();
  });

  const goPwChange = () => {
    if(confirm('비밀번호를 변경하겠습니까? (이메일 인증 후 비밀번호 재설정)')){
      router.push({ name: 'searchpw', query: { userId: userId.value, userTel: userTel.value, userEmail: userEmail.value } });
    } else {
      return
    }
  }
  const formatPhone = (phone) => {
  let digits = phone.replace(/\D/g, ''); // 숫자만 추출

  if (digits.length > 11) {
    digits = digits.slice(0, 11);
  }

  if (digits.length < 4) {
    return digits;
  } else if (digits.length < 8) {
    return `${digits.slice(0, 3)}-${digits.slice(3)}`;
  } else {
    return `${digits.slice(0, 3)}-${digits.slice(3, 7)}-${digits.slice(7)}`;
  }
};

// 전화번호 변경
const isEditingPhone = ref(false)
const newPhone = ref('')
const newPhoneInput = ref(null)

const requestPhoneVerification = async() => {
  const phonePattern = /^\d{3}-\d{3,4}-\d{4}$/;

  if (!phonePattern.test(newPhone.value)) {
    errors.value.newPhone = '유효한 전화번호 형식(000-0000-0000)을 입력해주세요.'
    newPhoneInput.value.focus()
    return
  }
  try {
      const response = await axios.post('http://localhost:80/mypage/phoneUpdate', {
        u_phone: newPhone.value
      })
      console.log('전화번호 수정 응답:', response.data)
      alert('전화번호가 수정되었습니다.')
      userTel.value = newPhone.value;
      isEditingPhone.value = false;
    } catch (error) {
      console.error('전화번호 수정 실패:', error)
      alert('전화번호 수정에 실패했습니다.')
    }
}

const cancelPhoneEdit = () => {
  newPhone.value = ''
  isEditingPhone.value = false
}
const startPhoneEdit = () => {
  newPhone.value = '';
  isEditingPhone.value = true;
};

const isEditingNickname = ref(false);
const newNickname = ref('');
const nicknameInput = ref(null)

const updateNickname = async () => {

  const nicknamePattern = /^\S{5,20}$/

  if (!newNickname.value.trim()) {
    alert('닉네임을 입력해주세요.');
    return;
  }

  //닉네임 체크
  if (!nicknamePattern.test(newNickname.value)) {
    errors.value.newNickname = '닉네임은 공백 없이 5~20자여야 합니다.'
    nicknameInput.value.focus()
    return
  }

  try {
    const response = await axios.post('http://localhost:80/mypage/nicnameUpdate', {
      u_nicname: newNickname.value
    });
    console.log('닉네임 수정 응답:', response.data);
    alert('닉네임이 수정되었습니다.');
    userInfo.value.U_NICNAME = newNickname.value;
    isEditingNickname.value = false;
  } catch (error) {
    console.error('닉네임 수정 실패:', error);
    alert('닉네임 수정에 실패했습니다.');
  }
};
const startNicknameEdit = () => {
  newNickname.value = '';
  isEditingNickname.value = true;
};

const cancelNicknameEdit = () => {
  newNickname.value = '';
  isEditingNickname.value = false;
};

// 회원 탈퇴
const userWithdraw = async () => {
  try {
    const response = await axios.post('http://localhost:80/mypage/userWithdraw');
    console.log('회원 탈퇴 응답:', response.data);
    if(confirm('블라블라를 탈퇴하시겠습니까?')){
        alert('회원 탈퇴가 완료되었습니다.\n 작성하신 게시글 및 댓글 중 개인정보가 포함되지 않은 내용은 삭제되지 않고 유지됩니다.');
        router.push('/').then(() => {
          window.location.reload()
        })
      } else {
        return
      }
  } catch (error) {
    console.error('회원 탈퇴 실패:', error);
    alert('회원 탈퇴에 실패했습니다.');
  }
};

watch(newNickname, () => {
    errors.value.newNickname = ''
  })
watch(newPhone, () => {
  errors.value.newPhone = ''
})
watch(newPhone, (val) => {
  newPhone.value = formatPhone(val);
});
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

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 23px 43px;
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
input[type="number"] {
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
.form-group input[type="text"] {
  width: 100%;
  box-sizing: border-box;
}

.edit-view {
  display: flex;
  align-items: center;
  gap: 10px;
}

.edit-view input[type="text"] {
  flex: 1;
}

.change-password-btn {
  padding: 6px 12px;
  background-color: black;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  white-space: nowrap;
}

.change-password-btn.gray {
  background-color: gray;
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