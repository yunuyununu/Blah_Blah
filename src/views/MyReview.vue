<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col">
          <div class="review-header">
            <a>나의리뷰<span style="font-size: 16px; color: #888;">({{review.length}})</span></a>
            <div v-if="reviewCheck.U_REVIEW === 'N'">
              <button class="review-btn" @click="goToWriteReview">
                리뷰 작성하기
              </button>
            </div>
          </div>
          <div class="ratings">
              <div v-if="review.length === 0" style="margin-top: 1rem; color: #888;">
                작성된 리뷰가 없습니다.
              </div>  
                <div v-else>
                  <div v-for="item in review" :key="item.r_idx" class="review-box">
                      <div class="review-body">
                        <img :src="getImageUrl(item.c_logo)" alt="로고" class="logo" />
                        <!-- 왼쪽: 별점 -->
                        <div class="review-score">
                          <div class="score-number" >{{ item.r_star.toFixed(1) }}</div>
                          <div class="score-stars">{{ '★'.repeat(item.r_star) }}{{ '☆'.repeat(5 - item.r_star) }}</div>
                        </div>

                        <!-- 오른쪽: 리뷰내용 -->
                        <div class="review-main">
                          <div class="review-title-row">
                            <h3 class="review-title">“{{ item.r_title }}”</h3>
                          </div>
                          <div class="review-meta">
                            {{ getRworkLabel(item.r_work) }} ｜ {{ item.u_nicname }} ｜ {{ item.c_nicname }} ｜ {{ item.r_date }}
                          </div>
                          <div class="review-section">
                            <strong>내용</strong>
                            <p>{{ item.r_content }}</p>
                          </div>

                        </div>
                      </div>
                      <div style="text-align: right;">
                        <button class="btn btn-outline-secondary" @click="goToUpdateReview">수정</button>
                      </div>
                       <!-- 리뷰 수정 모달 -->
                        <div class="modal-overlay" v-if="showReviewUpdateModal">
                          <div class="modal-content">
                            <div class="modal-header">
                              <h2>리뷰 수정</h2>
                              <button class="close-btn" @click="showModal = false">×</button>
                            </div>

                            <div class="modal-body">
                              <input />
                              <label>회사</label>
                              <input :value="item.r_idx" readonly/>
                              <label>별점 선택</label>
                              <div class="star-rating">
                                <span
                                  v-for="star in 5"
                                  :key="star"
                                  @click="form.r_star = star"
                                  class="star"
                                  :class="{ active: form.r_star >= star }"
                                >★</span>
                              </div>

                              <div class="row-inline">
                                <label class="inline-label">소속</label>
                                <div class="radio-group">
                                  <label class="radio-item">
                                    <input type="radio" value="CURRENT" v-model="item.r_work" />
                                    현직원
                                  </label>
                                  <label class="radio-item">
                                    <input type="radio" value="FORMER" v-model="item.r_work" />
                                    전직원
                                  </label>
                                </div>
                              </div>

                              <label>제목</label>
                              <input type="text" v-model="item.r_title" />

                              <label>내용</label>
                              <textarea rows="5" v-model="item.r_content"></textarea>
                            </div>

                            <div class="modal-footer">
                              <button class="submit-btn" @click="submitReview">작성 완료</button>
                            </div>
                          </div>
                        </div>
                    </div>
                </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- 리뷰 작성 모달 -->
<div class="modal-overlay" v-if="showModal">
  <div class="modal-content">
    <div class="modal-header">
      <h2>리뷰 작성</h2>
      <button class="close-btn" @click="showModal = false">×</button>
    </div>

    <div class="modal-body">
      <label>회사</label>
      <input :value="reviewCheck.C_NAME" readonly/>
      <label>별점 선택</label>
      <div class="star-rating">
        <span
          v-for="star in 5"
          :key="star"
          @click="form.r_star = star"
          class="star"
          :class="{ active: form.r_star >= star }"
        >★</span>
      </div>

      <div class="row-inline">
        <label class="inline-label">소속</label>
        <div class="radio-group">
          <label class="radio-item">
            <input type="radio" value="CURRENT" v-model="form.r_work" />
            현직원
          </label>
          <label class="radio-item">
            <input type="radio" value="FORMER" v-model="form.r_work" />
            전직원
          </label>
        </div>
      </div>

      <label>제목</label>
      <input type="text" v-model="form.r_title" />

      <label>내용</label>
      <textarea rows="5" v-model="form.r_content"></textarea>
    </div>

    <div class="modal-footer">
      <button class="submit-btn" @click="submitReview">작성 완료</button>
    </div>
  </div>
</div>
  </template>
  
  <script setup>
import { onMounted, ref } from 'vue';
import axios from 'axios';


const getImageUrl = (filename) => {
  if(filename != "") {
    return `https://storage.googleapis.com/blah_blah_bucket/${filename}`
  } else {
    return `https://storage.googleapis.com/blah_blah_bucket/no_image.png`
  }
}

const review = ref([])
const reviewCheck = ref([])
const getRworkLabel = (code) => {
  switch (code) {
    case 'CURRENT': return '현직원';
    case 'FORMER': return '전직원';
    default: return '기타';
  }
};

const fetchReviewCheck = async () => {
  try {
    const res = await axios.get(`http://localhost:80/mypage/myreviewCheck`);
    reviewCheck.value = res.data;
    form.value.r_c_idx = reviewCheck.value.C_IDX;
    console.log("리뷰체크=>",form.value.r_c_idx)
  } catch (err) {
    console.error('리뷰작성유무 로딩 실패:', err);
  }
};

const fetchReviewList = async () => {
  try {
    const res = await axios.get(`http://localhost:80/mypage/myreviewList`);
    review.value = res.data;
  } catch (err) {
    console.error('리뷰 로딩 실패:', err);
  }
};

onMounted(() => {
  fetchReviewList();
  fetchReviewCheck();
});

const showModal = ref(false)
const showReviewUpdateModal = ref(false)

const form = ref({
  r_c_idx: '',
  r_star: 0,
  r_work: '',
  r_title: '',
  r_content: ''
})
const goToWriteReview = () => {
  showModal.value = true
}

const goToUpdateReview = () => {
  showReviewUpdateModal.value = true
}

const submitReview = () => {
  try {
    axios.post('http://localhost:80/mypage/reviewInsert', form.value)
    console.log("리뷰작성시 입력데이터==>", form.value)
    alert('리뷰가 등록되었습니다.')
    showModal.value = false
    fetchReviewList()
    fetchReviewCheck()
  } catch (err) {
    console.error('리뷰 등록 실패:', err)
    alert('리뷰 등록 중 오류가 발생했습니다.')
  }
}
  </script>
  
  <style scoped>
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

.review-btn {
  background-color: #333;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
}
.review-write-btn-wrapper {
  text-align: right;
  margin: 1rem 0;
}
.review-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 1.5rem;
  border-radius: 10px;
  width: 400px;
  max-width: 90%;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
}

.modal-body label {
  display: block;
  margin-top: 1rem;
  font-weight: bold;
}

.modal-body input,
.modal-body textarea {
  width: 100%;
  margin-top: 0.5rem;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.star-rating {
  margin-top: 0.5rem;
  font-size: 1.5rem;
  color: #ccc;
  cursor: pointer;
}

.star-rating .star.active {
  color: #FFD700;
}

.modal-footer {
  text-align: right;
  margin-top: 1.5rem;
}

.submit-btn {
  background: #333;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}
.row-inline {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin: 0.5rem 0;
}

.inline-label {
  font-weight: bold;
  white-space: nowrap;
}

.radio-group {
  display: flex;
  gap: 1.5rem;
}

.radio-item {
  display: flex;
  align-items: center;
  gap: 0.3rem;
  cursor: pointer;
}


  </style>