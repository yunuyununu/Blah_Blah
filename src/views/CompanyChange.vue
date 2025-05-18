<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col">
          <form class="company-change-form" @submit.prevent="submitRequest">
            <!-- 회사명 -->
            <div class="form-group">
              <label>회사명 (한글/영문)</label>
              <input type="text" v-model="form.companyName" required />
            </div>

            <!-- 대표 사진 -->
            <div class="form-group">
              <label>회사 대표 사진</label>
              <input type="file" accept="image/*" @change="handleFileChange" required />
            </div>

            <!-- 업계 -->
            <div class="form-group">
              <label>업계 (예: IT업, 제조업)</label>
              <input type="text" v-model="form.industry" required />
            </div>

            <!-- 설립일자 -->
            <div class="form-group">
              <label>설립일자 (8자리, 예: 20240101)</label>
              <input type="text" v-model="form.established" maxlength="8" required />
            </div>

            <!-- 회사 인원 -->
            <div class="form-group">
              <label>회사 인원 수</label>
              <input type="number" v-model.number="form.employees" min="1" required />
            </div>

            <!-- 사업자등록번호 -->
            <div class="form-group">
              <label>사업자등록번호 (10자리, 예: 1234567890)</label>
              <input type="text" v-model="form.registrationNumber" maxlength="10" required />
            </div>
            <br>
            <div style="text-align: center;">
              <button type="submit" class="submit-btn">신청하기</button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script setup>
import { reactive } from 'vue';

const form = reactive({
  companyName: '',
  file: null,
  industry: '',
  established: '',
  employees: 0,
  registrationNumber: '',
});

const handleFileChange = (e) => {
  form.file = e.target.files[0];
};

const formatDate = (yyyymmdd) => {
  return `${yyyymmdd.slice(0, 4)}-${yyyymmdd.slice(4, 6)}-${yyyymmdd.slice(6, 8)}`;
};

const formatRegNum = (num) => {
  return `${num.slice(0, 3)}-${num.slice(3, 5)}-${num.slice(5, 10)}`;
};

const submitRequest = () => {
  if (!form.file) {
    alert('대표 사진을 첨부해주세요.');
    return;
  }
  if (form.established.length !== 8 || isNaN(form.established)) {
    alert('설립일자는 숫자 8자리로 입력해주세요.');
    return;
  }
  if (form.registrationNumber.length !== 10 || isNaN(form.registrationNumber)) {
    alert('사업자등록번호는 숫자 10자리로 입력해주세요.');
    return;
  }

  const formattedDate = formatDate(form.established);
  const formattedRegNum = formatRegNum(form.registrationNumber);
  const displayName = form.employees >= 100 ? form.companyName : '새회사';

  const formData = new FormData();
  formData.append('companyName', displayName);
  formData.append('industry', form.industry);
  formData.append('established', formattedDate);
  formData.append('registrationNumber', formattedRegNum);
  formData.append('file', form.file);
  formData.append('employees', form.employees);

  console.log('제출 데이터:', Object.fromEntries(formData.entries()));
  alert(`"${displayName}" 회사 변경 신청이 제출되었습니다.`);
};
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
</style>

