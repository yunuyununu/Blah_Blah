<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col">
          <a>나의리뷰<span style="font-size: 16px; color: #888;">({{ totalCount }}개)</span></a>
          <div class="ratings">
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

                          <div class="review-section" v-if="userStore.isLogin == true">
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
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script setup>
  
  
  </script>
  
  <style scoped>

  </style>