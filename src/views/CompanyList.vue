<template>
  <div class="content">
    <div class="container">
      <div class="search-box">
        <input type="text" placeholder="회사명을 입력하세요" v-model="searchKeyword" @keyup.enter="searchCompanies" ref="searchInput"/>
        <button @click="searchCompanies">검색</button>
      </div>

      <div class="no-result-text" style="text-align: center;">
        찾으시는 회사가 없나요? 
        <span class="underline-link" @click="goCompanyInsert">궁금한 회사를 직접 신청해주세요!</span>
      </div>

      <div class="company-grid">
        <div
          v-for="company in companyList"
          :key="company.c_idx"
          class="company-card"
          @click="goCompanyDetails(company.c_idx)"
        >
          <img :src="getImageUrl(company.c_logo)" alt="로고" class="company-logo" />
          <h4>{{ company.c_name }}</h4>
          <p>업계: {{ company.c_industry }}</p>
          <p>설립일: {{ company.c_est }}</p>
        </div>
      </div>

      <div class="pagination">
        <button v-if="offset > 0" @click="prevPage" :disabled="offset === 0">이전</button>
        <button v-if="companyList.length === limit" @click="nextPage">다음</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const companyList = ref([])
const searchKeyword = ref('')
const router = useRouter()

const limit = 18
const offset = ref(0)

const searchInput = ref(null)

const getImageUrl = (filename) => {
  if (filename.startsWith('https')) {
    return filename;
  } else if (filename !== "") {
    return `https://storage.googleapis.com/blah_blah_bucket/${filename}`;
  } else {
    return `https://storage.googleapis.com/blah_blah_bucket/no_image.png`;
  }
};

// 검색어에 따른 회사 리스트 조회 함수
const fetchCompanies = async (keyword = '') => {
  const response = await axios.get('http://localhost:80/company/list', {
    params: {
      limit,
      offset: offset.value,
      c_name: keyword.trim(),
    }
  })
  companyList.value = response.data
}

// 검색 버튼 클릭 시 호출할 함수
const searchCompanies = () => {
  if (!searchKeyword.value.trim()) {
    // 검색어가 없으면 input에 포커스 주기
    searchInput.value.focus()
    return
  }
  offset.value = 0
  fetchCompanies(searchKeyword.value)
}

const goCompanyDetails = (c_idx) => {
  router.push({ name: 'companydetails', params: { c_idx } });
};

const nextPage = () => {
  if (companyList.value.length === limit) {
    offset.value += limit
    fetchCompanies(searchKeyword.value)
    //window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

const prevPage = () => {
  if (offset.value >= limit) {
    offset.value -= limit
    fetchCompanies(searchKeyword.value)
    //window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

// 초기 전체 리스트 호출 (검색어 없을 때)
onMounted(() => {
  fetchCompanies()
})

import { useUserStore } from '@/store/userStore'
const userStore = useUserStore()

const goCompanyInsert = () => {
  if (!userStore.isLogin) {
    alert('로그인 후 이용해주세요.')
    return
  }
  router.push('/company/companyInsert')
}
</script>

<style scoped>
.company-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  margin-top: 20px;
}

.company-card {
  border: 1px solid #ddd;
  padding: 16px;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}
.company-card:hover {
  transform: translateY(-5px); /* 살짝 떠오르게 */
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15); /* 그림자 강조 */
}

.company-logo {
  width: 100%;
  max-width: 200px;
  height: 150px;
  object-fit: contain;
  margin-bottom: 12px;
  border-radius: 4px;
  background-color: #f9f9f9;
}


.search-box {
  margin-bottom: 12px;
  display: flex;
  gap: 8px;
}

.search-box input {
  flex-grow: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  gap: 12px;
}
.underline-link {
  text-decoration: underline;
  color: #007bff; /* 원하는 링크 색상 */
  cursor: pointer;
}

.search-box button {
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-box button:hover {
  background-color: #0056b3;
}
/* src/style.css - 스크롤 애니메이션 완전 차단 */
html, body {
  scroll-behavior: auto !important;
}

* {
  scroll-behavior: auto !important;
}

/* Vuetify가 적용하는 smooth scroll도 차단 */
.v-application {
  scroll-behavior: auto !important;
}

</style>
