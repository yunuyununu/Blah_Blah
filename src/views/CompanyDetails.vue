<template>
  <div class="company-detail" v-if="companyDetails">
    <div class="header">
      <img :src="getImageUrl(companyDetails.C_LOGO)" alt="로고" class="logo" />
      <div class="info">
        <h1>{{ companyDetails.C_NAME }}</h1>
        <p class="rating">
          ⭐ {{ 1.2 }} ({{ 0 }}개 리뷰)
        </p>
        <button class="like-btn">👍</button>
        <button class="dislike-btn">👎</button>
      </div>
    </div>

    <div class="summary-box">
      <h2>회사소개</h2>
      <ul>
        <!-- <li><strong>홈페이지:</strong> <a :href="companyDetails.website" target="_blank">{{ companyDetails.website }}</a></li> -->
        <li><strong>업계:</strong> {{ companyDetails.C_INDUSTRY }}</li>
        <li><strong>설립:</strong> {{ companyDetails.C_EST }}</li>
        <li><strong>직원수:</strong> {{ companyDetails.C_NUMBER }}</li>
        <!-- <li><strong>연봉정보:</strong> {{ companyDetails.salary }}</li> -->
      </ul>
    </div>

    <div class="ratings">
      <h2>회사 리뷰</h2>
      <p class="overall">⭐ 1.2 / 5</p>
      <div class="rating-grid">
        <!-- <div v-for="item in companyDetails.evaluations" :key="item.label" class="item">
          {{ item.label }}: ⭐ {{ item.score }}
        </div> -->
      </div>
    </div>

    <!-- <div class="review">
      <h3>대표 리뷰</h3>
      <p class="highlight">“{{ company.representativeReview.text }}”</p>
      <p class="sub">
        - {{ company.representativeReview.author }} · {{ company.representativeReview.date }}
      </p>
      <p class="pros"><strong>장점:</strong> {{ company.representativeReview.advantage }}</p>
    </div> -->
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router';
import { onMounted, ref } from 'vue';
import axios from 'axios';

const route = useRoute();
const companyDetails = ref(null);
const cidx = route.params.c_idx;

const fetchCompanyDetail = async () => {
  try {
    const res = await axios.get(`http://localhost:80/company/details`, {
      params: { c_idx: cidx },
    });
    companyDetails.value = res.data;
  } catch (err) {
    console.error('게시글 로딩 실패:', err);
  }
};

const getImageUrl = (filename) => {
  if(filename != "") {
    return `https://storage.googleapis.com/blah_blah_bucket/${filename}`
  } else {
    return `https://storage.googleapis.com/blah_blah_bucket/no_image.png`
  }
}

onMounted(() => {
  fetchCompanyDetail();
});
</script>

<style scoped>
.company-detail {
  max-width: 800px;
  margin: 0 auto;
  font-family: 'Arial', sans-serif;
  padding: 20px;
}

.header {
  display: flex;
  align-items: center;
  gap: 20px;
}

.logo {
  width: 80px;
  height: 80px;
  object-fit: contain;
  border-radius: 8px;
}

.info h1 {
  margin: 0;
  font-size: 24px;
}

.rating {
  color: #f39c12;
  font-weight: bold;
}

.like-btn,
.dislike-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 20px;
}

.summary-box {
  background: #f9f9f9;
  padding: 16px;
  border-radius: 8px;
  margin-top: 24px;
}

.summary-box ul {
  list-style: none;
  padding: 0;
}

.summary-box li {
  margin-bottom: 8px;
}

.ratings {
  margin-top: 32px;
}

.rating-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-top: 8px;
}

.item {
  background: #eef1f4;
  padding: 8px;
  border-radius: 6px;
}

.review {
  margin-top: 32px;
}

.highlight {
  font-style: italic;
  font-weight: bold;
}

.sub {
  font-size: 14px;
  color: #666;
}
</style>
