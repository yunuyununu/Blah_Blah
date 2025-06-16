<template>
  <div class="board-container">
    <!-- 검색창 -->
    <div class="search-container">
      <input
        v-model="searchKeyword"
        type="text"
        placeholder="회사명을 입력하세요"
        @keyup.enter="onSearch"
        class="search-input"
        ref="searchInput"
      />
      <button @click="onSearch">검색</button>
      <button v-if="searchKeyword.trim() !== ''" @click="clearSearch" class="clear-btn">전체보기</button>
    </div>
<div class="no-result-text" style="text-align: center;">
        찾으시는 회사가 없나요? 
        <span class="underline-link" @click="goCompanyInsert">궁금한 회사를 직접 신청해주세요!</span>
      </div>
      <br>
    <!-- 회사 카드 목록 -->
    <div v-if="companies.length > 0" class="card-grid">
      <div class="card" v-for="item in companies" :key="item.C_IDX" @click="goCompanyDetails(item.C_IDX)">
  <div class="logo-wrapper">
    <img :src="getImageUrl(item.C_LOGO)" alt="회사 로고" class="company-logo" />
  </div>
  <div class="card-title">{{ item.C_NAME }}</div>
  <div class="card-content">업계: {{ item.C_INDUSTRY }}</div>
  <div class="card-content">설립일: {{ item.C_EST }}</div>
</div>

    </div>

    <!-- 페이지네이션 -->
    <div v-if="totalPages > 1" class="pagination">
      <button @click="changePage(1)" :disabled="currentPage === 1">처음</button>
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">이전</button>

      <button
        v-for="page in pageNumbers"
        :key="page"
        @click="changePage(page)"
        :class="{ active: currentPage === page }"
      >
        {{ page }}
      </button>

      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">다음</button>
      <button @click="changePage(totalPages)" :disabled="currentPage === totalPages">마지막</button>
    </div>

    <!-- 검색결과 없을 때 -->
    <div v-if="companies.length === 0" class="no-results">
      <p v-if="searchKeyword.trim() !== ''">검색결과가 없습니다!</p>
      <p v-else>등록된 회사가 없습니다.</p>
      <p v-if="searchKeyword.trim() !== ''">검색어를 다시 확인해주세요.</p>
    </div>

    <!-- 로딩 -->
    <div v-if="loading" class="loading">
      <p>로딩중...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useUserStore } from '@/store/userStore'

const companies = ref([])
const currentPage = ref(1)
const totalPages = ref(1)
const totalCount = ref(0)
const pageSize = 15
const searchKeyword = ref('')
const searchInput = ref(null)
const loading = ref(false)
const router = useRouter()
const userStore = useUserStore()

// 이미지 URL 처리
const getImageUrl = (filename) => {
  if (filename.startsWith('https')) {
    return filename;
  } else if (filename !== "") {
    return `https://storage.googleapis.com/blah_blah_bucket/${filename}`;
  } else {
    return `https://storage.googleapis.com/blah_blah_bucket/no_image.png`;
  }
};


// 페이지 번호 계산
const pageNumbers = computed(() => {
  const maxVisible = 5
  const start = Math.max(1, currentPage.value - Math.floor(maxVisible / 2))
  const end = Math.min(totalPages.value, start + maxVisible - 1)
  const adjustedStart = Math.max(1, end - maxVisible + 1)
  return Array.from({ length: end - adjustedStart + 1 }, (_, i) => adjustedStart + i)
})

// 회사 목록 조회
const fetchCompanies = async (resetPage = false) => {
  try {
    loading.value = true
    if (resetPage) currentPage.value = 1

    const res = await axios.get('http://localhost:80/company/list', {
      params: {
        searchKeyword: searchKeyword.value.trim(),
        page: currentPage.value,
        pageSize: pageSize,
      }
    })
    companies.value = res.data.list || []
    totalPages.value = res.data.totalPages || 1
    totalCount.value = res.data.totalCount || 0
  } catch (err) {
    console.error('회사 조회 실패:', err)
    companies.value = []
    totalPages.value = 1
    totalCount.value = 0
  } finally {
    loading.value = false
  }
}

// 페이지 변경
const changePage = async (page) => {
  if (page < 1 || page > totalPages.value || page === currentPage.value) return
  currentPage.value = page
  await fetchCompanies()
}

// 검색
const onSearch = async () => {
  if (!searchKeyword.value.trim()) {
    searchInput.value.focus()
    return
  }
  await fetchCompanies(true)
}

// 검색 초기화
const clearSearch = async () => {
  searchKeyword.value = ''
  await fetchCompanies(true)
}

// 회사 상세 페이지 이동
const goCompanyDetails = (c_idx) => {
  router.push({ name: 'companydetails', params: { c_idx } })
}

// 회사 등록 요청 페이지 이동
const goCompanyInsert = () => {
  if (!userStore.isLogin) {
    alert('로그인 후 이용해주세요.')
    return
  }
  router.push('/company/companyInsert')
}

onMounted(() => {
  fetchCompanies()
})
</script>

<style scoped>
/* 필요한 경우 boardList.vue 스타일 복사해서 재사용 */
</style>


<style scoped>
.board-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
  font-family: 'Pretendard', sans-serif;
  color: #333;
}

.search-container {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 24px;
}

.search-input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 16px;
}

.search-container button {
  padding: 10px 18px;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.2s;
}

.search-container button:hover {
  opacity: 0.9;
}

.search-container button:first-of-type {
  background-color: #007bff;
  color: white;
}

.clear-btn {
  background-color: #6c757d;
  color: white;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
  margin-bottom: 32px;
}

.card {
  background: white;
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  cursor: pointer;
  text-align: center;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.company-logo {
  width: 200px;
  height: 200px;
  object-fit: contain;
  margin-bottom: 12px;
}

.card-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 6px;
}

.card-content {
  font-size: 14px;
  color: #666;
  margin-bottom: 4px;
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 20px;
}

.pagination button {
  padding: 8px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  background: white;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
}

.pagination button:hover:not(:disabled) {
  background-color: #f0f0f0;
}

.pagination button.active {
  background-color: #007bff;
  color: white;
  border-color: #007bff;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.no-results, .loading {
  text-align: center;
  padding: 40px;
  color: #888;
  font-size: 15px;
}

.underline-link {
  color: #007bff;
  cursor: pointer;
  text-decoration: underline;
  font-weight: 500;
  margin-left: 6px;
}
.logo-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px; /* 이미지 높이에 맞춤 */
  margin-bottom: 12px;
}

.company-logo {
  max-width: 100%;
  max-height: 100%;
  object-fit: contain;
}


</style>
