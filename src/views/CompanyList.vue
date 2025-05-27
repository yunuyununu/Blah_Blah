<template>
  <div class="content">
    <div class="container">
      <div class="search-box">
        <input type="text" placeholder="회사명을 입력하세요" v-model="searchKeyword" />
      </div>
      <div class="no-result-text">
        찾으시는 회사가 없나요? <RouterLink to="/company/companyInsert" class="underline-link">궁금한 회사를 직접 신청해주세요!</RouterLink>
      </div>

      <div class="company-grid">
        <div
          v-for="company in filteredCompanies"
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
        <button @click="prevPage" :disabled="offset === 0">이전</button>
        <button @click="nextPage">다음</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios';


const company = ref([])
const searchKeyword = ref('')

const router = useRouter()


const limit = 18
const offset = ref(0)


const getImageUrl = (filename) => {
  if(filename != "") {
    return `https://storage.googleapis.com/blah_blah_bucket/${filename}`
  } else {
    return `https://storage.googleapis.com/blah_blah_bucket/no_image.png`
  }
}


const fetchCompanies = async () => {
  const response = await axios.get('http://localhost:80/company/list', {
    params: {
      limit,
      offset: offset.value
    }
  })
  company.value = response.data
}

const goCompanyDetails = async (c_idx) => {
  router.push({ name: 'companydetails', params: { c_idx } });
};

const nextPage = () => {
  offset.value += limit
  fetchCompanies()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const prevPage = () => {
  if (offset.value >= limit) {
    offset.value -= limit
    fetchCompanies()
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

const filteredCompanies = computed(() => {
  if (!searchKeyword.value) return company.value
  return company.value.filter(c =>
    c.c_name.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

onMounted(() => {
  fetchCompanies()
})
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
}

.search-box input {
  width: 100%;
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
</style>
