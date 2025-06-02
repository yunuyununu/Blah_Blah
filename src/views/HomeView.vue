<template>
  <div class="main-wrapper">
    <!-- 검색창 -->
    <div class="search-bar">
      <span class="icon"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search" viewBox="0 0 16 16">
  <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001q.044.06.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1 1 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0"/>
</svg></span>
      <input type="text" placeholder="관심있는 내용을 검색해보세요!" />
    </div>

    <div class="content-wrapper">
      <!-- 좌측: 주간/월간 토픽 -->
      <div class="topics-wrapper">
        <!-- 주간 베스트 -->
        <div class="topic-box">
          <div class="header">
            <h2>게시글 주간 토픽 베스트</h2>
          </div>
          <ul class="post-list">
            <li v-for="week in weeklyPosts" :key="week.b_idx" class="post-item">
              <a
                href="#"
                @click.prevent="goToBoardDetail(week.b_idx)"
                class="monthly-link"
              >
              <div class="left">
                <span class="category">{{ week.category }}</span>
                <span class="title">{{ week.b_title }}</span>
              </div>
              <div class="right">
                <span class="icon"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-hand-thumbs-up-fill" viewBox="0 0 16 16">
                  <path d="M6.956 1.745C7.021.81 7.908.087 8.864.325l.261.066c.463.116.874.456 1.012.965.22.816.533 2.511.062 4.51a10 10 0 0 1 .443-.051c.713-.065 1.669-.072 2.516.21.518.173.994.681 1.2 1.273.184.532.16 1.162-.234 1.733q.086.18.138.363c.077.27.113.567.113.856s-.036.586-.113.856c-.039.135-.09.273-.16.404.169.387.107.819-.003 1.148a3.2 3.2 0 0 1-.488.901c.054.152.076.312.076.465 0 .305-.089.625-.253.912C13.1 15.522 12.437 16 11.5 16H8c-.605 0-1.07-.081-1.466-.218a4.8 4.8 0 0 1-.97-.484l-.048-.03c-.504-.307-.999-.609-2.068-.722C2.682 14.464 2 13.846 2 13V9c0-.85.685-1.432 1.357-1.615.849-.232 1.574-.787 2.132-1.41.56-.627.914-1.28 1.039-1.639.199-.575.356-1.539.428-2.59z"/>
                </svg> {{ week.like_COUNT }}</span>
                <span class="icon"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chat" viewBox="0 0 16 16">
                  <path d="M2.678 11.894a1 1 0 0 1 .287.801 11 11 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8 8 0 0 0 8 14c3.996 0 7-2.807 7-6s-3.004-6-7-6-7 2.808-7 6c0 1.468.617 2.83 1.678 3.894m-.493 3.905a22 22 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a10 10 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9 9 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105"/>
                </svg> {{ week.comment_COUNT }}</span>
              </div>
            </a>
            </li>
          </ul>
        </div>

        <!-- 월간 베스트 -->
        <div class="topic-box">
          <div class="header">
            <h2>게시글 월간 토픽 베스트</h2>
          </div>
          <ul class="post-list">
    <li v-for="month in monthlyPosts" :key="month.b_id" class="post-item">
      <a href="#" @click.prevent="goToBoardDetail(month.b_id)" class="monthly-link">
        <div class="left">
          <span class="category">{{ month.category }}</span>
          <span class="title">{{ month.b_title }}</span>
        </div>
        <div class="right">
          <span class="icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-hand-thumbs-up-fill" viewBox="0 0 16 16">
              <path d="M6.956 1.745C7.021.81 7.908.087 8.864.325l.261.066c.463.116.874.456 1.012.965.22.816.533 2.511.062 4.51a10 10 0 0 1 .443-.051c.713-.065 1.669-.072 2.516.21.518.173.994.681 1.2 1.273.184.532.16 1.162-.234 1.733q.086.18.138.363c.077.27.113.567.113.856s-.036.586-.113.856c-.039.135-.09.273-.16.404.169.387.107.819-.003 1.148a3.2 3.2 0 0 1-.488.901c.054.152.076.312.076.465 0 .305-.089.625-.253.912C13.1 15.522 12.437 16 11.5 16H8c-.605 0-1.07-.081-1.466-.218a4.8 4.8 0 0 1-.97-.484l-.048-.03c-.504-.307-.999-.609-2.068-.722C2.682 14.464 2 13.846 2 13V9c0-.85.685-1.432 1.357-1.615.849-.232 1.574-.787 2.132-1.41.56-.627.914-1.28 1.039-1.639.199-.575.356-1.539.428-2.59z"/>
            </svg> {{ month.like_COUNT }}
          </span>
          <span class="icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chat" viewBox="0 0 16 16">
              <path d="M2.678 11.894a1 1 0 0 1 .287.801 11 11 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8 8 0 0 0 8 14c3.996 0 7-2.807 7-6s-3.004-6-7-6-7 2.808-7 6c0 1.468.617 2.83 1.678 3.894m-.493 3.905a22 22 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a10 10 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9 9 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105"/>
            </svg> {{ month.comment_COUNT }}
          </span>
        </div>
      </a>
    </li>
  </ul>
        </div>
      </div>

      <!-- 우측: 실시간 인기 회사 -->
      <div class="ranking-box">
        <div class="header">
          <h2>평점 높은 회사 순위</h2>
        </div>
        <ol class="rank-list">
          <li
            v-for="(company, index) in topCompanies"
            :key="company.c_idx"
            class="rank-item"
          >
            <span class="rank-number">{{ index + 1 }}</span>
            <span class="rank-name">{{ company.c_name }}</span>
            <!-- <span class="rank-change" :class="getRankChangeClass(company.rank_change)">
              <span v-if="company.rank_change === 'up'">▲</span>
              <span v-else-if="company.rank_change === 'down'">▼</span>
              <span v-else>-</span>
            </span> -->
          </li>
        </ol>
        <div class="rank-info"></div>
      </div>
    </div>
  </div>
</template>


<script setup>
import { useRouter } from 'vue-router'
import { onMounted,ref } from 'vue'
import axios from 'axios';

const router = useRouter()

const weeklyPosts  = ref([]);
const monthlyPosts  = ref([]);
const topCompanies = ref([]);

const fetchWeeklyPosts  = async () => {
  const res = await axios.get('http://localhost:80/board/weeklyBest');
  weeklyPosts.value = res.data;
  console.log("주간목록=>",res)
}

const fetchMonthlyPosts  = async () => {
  const res = await axios.get('http://localhost:80/board/monthlyBest');
  monthlyPosts.value = res.data;
  console.log("월간목록=>",res)
}

const fetchTopCompanies = async () => {
  const res = await axios.get('http://localhost:80/company/score');
  topCompanies.value = res.data;
};

onMounted(async () => {
  await fetchWeeklyPosts();
  await fetchMonthlyPosts();
  await fetchTopCompanies();
});

const goToBoardDetail = async (url) => {

  let b_url = "/boarddetails/"+url;

  if (router.currentRoute.value.path === b_url) {
    // 동일한 경로일 경우 강제로 새로고침
    await router.replace({ path: '/_redirect' }) // 임시 페이지로 이동
    setTimeout(() => {
      router.replace({ path: b_url })
    }, 10)        // 다시 원래 페이지로 이동
  } else {
    router.push({ path: b_url })
  }
}
</script>

<style scoped>
.monthly-link {
  display: flex;
  justify-content: space-between;
  align-items: center;
  text-decoration: none;
  color: inherit;
  width: 100%;
}

.monthly-link:hover {
  background-color: #f9f9f9; /* 호버 효과 필요시 */
}
.main-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px;
  background: white;
}

.search-bar {
  display: flex;
  align-items: center;
  border: 1px solid #ccc;
  border-radius: 28px;
  padding: 14px 20px;
  width: 100%;
  max-width: 800px;
  font-size: 16px;
  margin-bottom: 32px;
  background: #fff;
}

.search-bar .icon {
  margin-right: 12px;
  color: #555;
  font-size: 18px;
}

.search-bar input {
  border: none;
  outline: none;
  flex: 1;
  font-size: 16px;
  color: #222;
}

.content-wrapper {
  display: flex;
  gap: 24px;
  width: 100%;
  max-width: 1200px;
  justify-content: center;
  align-items: flex-start;
  flex-wrap: wrap;
}

/* 좌측 베스트 토픽들 */
.topics-wrapper {
  flex: 1 1 700px;
  display: flex;
  flex-direction: column;
  gap: 24px;
}

/* 개별 박스 */
.topic-box, .ranking-box {
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 16px;
  padding: 24px;
  box-shadow: 0 4px 8px rgba(0,0,0,0.05);
}

.header h2 {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 16px;
}

.post-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.post-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
  font-size: 15px;
}

.left {
  display: flex;        
  flex-wrap: nowrap;    
  max-width: 400px;
  gap: 8px;             
  align-items: center;
}

.category {
  font-weight: 600;
  color: #888;
  font-size: 13px;
  white-space: nowrap;  
}

.title {
  color: #333;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.right {
  display: flex;
  gap: 10px;
  color: #999;
  font-size: 14px;
  align-items: center;
}

/* 우측 실시간 인기 */
.ranking-box {
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 12px;
  padding: 20px;
  width: 260px;
  font-family: sans-serif;
}

.rank-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.rank-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 6px 0;
  font-size: 14px;
  border-bottom: 1px solid #f3f3f3;
}

.rank-number {
 font-weight: bold;
  width: 20px;
}

.rank-name {
  flex: 1;
  margin-left: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
/* 변화 스타일 */
.rank-change.up {
  color: red;
}
.rank-change.down {
  color: blue;
}
.rank-change.same {
  color: #999;
}

/* 하단 안내 */
.rank-info {
  font-size: 12px;
  color: #999;
  margin-top: 14px;
  display: flex;
  align-items: center;
  gap: 4px;
}

</style>

