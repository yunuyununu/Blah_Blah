<template>
  <div class="company-detail" v-if="companyDetails">
    <div class="header">
      <img :src="getImageUrl(companyDetails.C_LOGO)" alt="로고" class="logo" />
      <div class="info">
        <h1>{{ companyDetails.C_NAME }}</h1>
        <p class="rating">
          ⭐ {{ star }} ({{ totalCount }}개 리뷰)
        </p>
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
      <h2>회사 리뷰 <span style="font-size: 16px; color: #888;">({{ totalCount }}개)</span></h2>
         <div v-if="review.length === 0" style="margin-top: 1rem; color: #888;">
          작성된 리뷰가 없습니다.
        </div>  
          <div v-else>
            <div v-for="item in review" :key="item.r_idx" class="review-box">
                <div class="review-body">
                  <!-- 왼쪽: 별점 -->
                  <div class="review-score">
                    <div class="score-number" >{{ item.r_star.toFixed(1) }}</div>
                    <div class="score-stars">{{ '★'.repeat(item.r_star) }}{{ '☆'.repeat(5 - item.r_star) }}</div>
                  </div>

                  <!-- 오른쪽: 리뷰내용 -->
                  <div class="review-main">
                    <div class="review-title-row">
                      <!-- <span class="badge">인기</span> -->
                      <h3 class="review-title">“{{ item.r_title }}”</h3>
                    </div>
                    <div class="review-meta">{{ item.r_work }} ｜ {{ item.u_nicname }} ｜ {{ item.c_nicname }} ｜ {{ item.r_date }}</div>

                    <div class="review-section" v-if="userStore.isLogin === true && reviewYN > 0">
                      <strong>내용</strong>
                      <p>{{ item.r_content }}</p>
                    </div>

                      <div v-else class="blur-container">
                        <p class="blurred-text">{{ item.r_content }}</p>
                        <div class="blur-overlay">
                          <div class="lock-icon">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-file-lock2-fill" viewBox="0 0 16 16">
                              <path d="M7 6a1 1 0 0 1 2 0v1H7z"/>
                              <path d="M12 0H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2m-2 6v1.076c.54.166 1 .597 1 1.224v2.4c0 .816-.781 1.3-1.5 1.3h-3c-.719 0-1.5-.484-1.5-1.3V8.3c0-.627.46-1.058 1-1.224V6a2 2 0 1 1 4 0"/>
                            </svg>
                          </div>
                          <p class="blur-message">내 회사 리뷰를 작성하고 전체 리뷰를 확인하세요!</p>
                          <button class="review-btn" @click="goWriteReview">내 회사 리뷰하기</button>
                        </div>
                      </div>

                  </div>
                </div>
              </div>
          </div>
    </div>

    <div class="pagination">
        <button v-if="offset > 0" @click="prevPage">이전</button>
        <button v-if="offset + limit < totalCount" @click="nextPage">다음</button>
      </div>
  </div>
</template>

<script setup>
import { useRoute,useRouter } from 'vue-router';
import { onMounted, ref } from 'vue';
import axios from 'axios';
import { useUserStore } from '@/store/userStore'

const userStore = useUserStore()
 const router = useRouter();

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
  if (filename.startsWith('https')) {
    return filename;
  } else if (filename !== "") {
    return `https://storage.googleapis.com/blah_blah_bucket/${filename}`;
  } else {
    return `https://storage.googleapis.com/blah_blah_bucket/no_image.png`;
  }
};

const limit = 5
const offset = ref(0)
const review = ref([])
const totalCount = ref(0)
const star = ref(0)

const fetchReviewList = async () => {
  try {
    const res = await axios.get(`http://localhost:80/company/reviewList`, {
      params: {
         r_c_idx: cidx,
         limit,
         offset: offset.value
      }
    });
    review.value = res.data.reviewlist;
    totalCount.value = res.data.totalCount;
    star.value = res.data.companyStar;
  } catch (err) {
    console.error('리뷰 로딩 실패:', err);
  }
};

const nextPage = () => {
  if (offset.value + limit < totalCount.value) {
    offset.value += limit
    fetchReviewList()
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

const prevPage = () => {
  if (offset.value >= limit) {
    offset.value -= limit
    fetchReviewList()
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

const reviewYN = ref(null)

const fetchReviewYN = async () => {
  try {
    const res = await axios.get(`http://localhost:80/company/reviewYN`);
    reviewYN.value = res.data;
  } catch (err) {
    console.error('리뷰 로딩 실패:', err);
  }
};

onMounted(() => {
  fetchCompanyDetail();
  fetchReviewList();
  if(userStore.isLogin === true) {
    fetchReviewYN();
  }
});

const goWriteReview = async () => {
  const confirmLogout = confirm('마이페이지로 이동하시겠습니까?')
      if (confirmLogout) {
          router.push('/mypage/myreview')
      } else {
        return
      }
}

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
.review-box {
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  background: #fff;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.03);
  min-height: 230px;
}

.review-body {
  display: flex;
  gap: 20px;
}

.review-score {
  width: 80px;
  text-align: center;
  flex-shrink: 0;
}

.score-number {
  font-size: 1.5rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 4px;
}

.score-stars {
  font-size: 14px;
  color: #FFD700;
}

.review-main {
  flex: 1;
}

.review-title-row {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-bottom: 6px;
}

.review-title {
  font-size: 1.1rem;
  font-weight: bold;
  margin: 0;
}

.badge {
  background-color: #ffe200;
  color: #333;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: bold;
}

.review-meta {
  font-size: 13px;
  color: #888;
  margin-bottom: 10px;
}

.review-section {
  margin-bottom: 10px;
}

.review-section p {
  white-space: pre-wrap;
  line-height: 1.6;
}


.review-section strong {
  display: inline-block;
  margin-bottom: 4px;
  color: #333;
}
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  gap: 12px;
}
.blurred-text {
  filter: blur(5px);
  user-select: none;
  pointer-events: none;
  margin: 0;
  min-height: 120px;
  background: #f7f7f7;
  border-radius: 6px;
  padding: 8px;
}

.blur-container {
  position: relative;
}

.blur-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  backdrop-filter: blur(0);
  background: rgba(255, 255, 255, 0.85);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  gap: 8px;
  z-index: 10;
  border-radius: 6px;
  padding: 1rem;
}

.lock-icon {
  width: 24px;
  height: 24px;
  color: #666;
}

.blur-message {
  font-weight: bold;
  color: #444;
  text-align: center;
}

.review-btn {
  background-color: #333;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}

</style>
