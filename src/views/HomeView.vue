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
                <span class="title" style="display: flex; align-items: center; gap: 4px;">
                  <template v-if="week.image_COUNT > 0">
                    <!-- SVG 아이콘 -->
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                      class="bi bi-image" viewBox="0 0 16 16">
                      <path d="M6.002 5.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0"/>
                      <path d="M2.002 1a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V3a2 2 0 0 0-2-2zm12 1a1 1 0 0 1 1 1v6.5l-3.777-1.947a.5.5 0 0 0-.577.093l-3.71 3.71-2.66-1.772a.5.5 0 0 0-.63.062L1.002 12V3a1 1 0 0 1 1-1z"/>
                    </svg>
                  </template>
                  {{ week.b_title }}
                </span>
              </div>
              <div class="right">
                <span class="icon"><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-suit-heart" viewBox="0 0 16 16">
  <path d="m8 6.236-.894-1.789c-.222-.443-.607-1.08-1.152-1.595C5.418 2.345 4.776 2 4 2 2.324 2 1 3.326 1 4.92c0 1.211.554 2.066 1.868 3.37.337.334.721.695 1.146 1.093C5.122 10.423 6.5 11.717 8 13.447c1.5-1.73 2.878-3.024 3.986-4.064.425-.398.81-.76 1.146-1.093C14.446 6.986 15 6.131 15 4.92 15 3.326 13.676 2 12 2c-.777 0-1.418.345-1.954.852-.545.515-.93 1.152-1.152 1.595zm.392 8.292a.513.513 0 0 1-.784 0c-1.601-1.902-3.05-3.262-4.243-4.381C1.3 8.208 0 6.989 0 4.92 0 2.755 1.79 1 4 1c1.6 0 2.719 1.05 3.404 2.008.26.365.458.716.596.992a7.6 7.6 0 0 1 .596-.992C9.281 2.049 10.4 1 12 1c2.21 0 4 1.755 4 3.92 0 2.069-1.3 3.288-3.365 5.227-1.193 1.12-2.642 2.48-4.243 4.38z"/>
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
    <li v-for="month in monthlyPosts" :key="month.b_idx" class="post-item">
      <a href="#" @click.prevent="goToBoardDetail(month.b_idx)" class="monthly-link">
        <div class="left">
          <span class="category">{{ month.category }}</span>
          <span class="title" style="display: flex; align-items: center; gap: 4px;">
            <template v-if="month.image_COUNT > 0">
              <!-- SVG 아이콘 -->
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                class="bi bi-image" viewBox="0 0 16 16">
                <path d="M6.002 5.5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0"/>
                <path d="M2.002 1a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V3a2 2 0 0 0-2-2zm12 1a1 1 0 0 1 1 1v6.5l-3.777-1.947a.5.5 0 0 0-.577.093l-3.71 3.71-2.66-1.772a.5.5 0 0 0-.63.062L1.002 12V3a1 1 0 0 1 1-1z"/>
              </svg>
            </template>
            {{ month.b_title }}
          </span>
        </div>
        <div class="right">
          <span class="icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-suit-heart" viewBox="0 0 16 16">
  <path d="m8 6.236-.894-1.789c-.222-.443-.607-1.08-1.152-1.595C5.418 2.345 4.776 2 4 2 2.324 2 1 3.326 1 4.92c0 1.211.554 2.066 1.868 3.37.337.334.721.695 1.146 1.093C5.122 10.423 6.5 11.717 8 13.447c1.5-1.73 2.878-3.024 3.986-4.064.425-.398.81-.76 1.146-1.093C14.446 6.986 15 6.131 15 4.92 15 3.326 13.676 2 12 2c-.777 0-1.418.345-1.954.852-.545.515-.93 1.152-1.152 1.595zm.392 8.292a.513.513 0 0 1-.784 0c-1.601-1.902-3.05-3.262-4.243-4.381C1.3 8.208 0 6.989 0 4.92 0 2.755 1.79 1 4 1c1.6 0 2.719 1.05 3.404 2.008.26.365.458.716.596.992a7.6 7.6 0 0 1 .596-.992C9.281 2.049 10.4 1 12 1c2.21 0 4 1.755 4 3.92 0 2.069-1.3 3.288-3.365 5.227-1.193 1.12-2.642 2.48-4.243 4.38z"/>
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
<div class="topic-box">
      <div class="header">
        <h2>투표 베스트</h2>
      </div>
      <div class="vote-best-wrapper">
        <div class="vote-card">
          <h3 class="vote-title">"오늘 점심 추천"</h3>
          <div class="user-info">
            <div class="profile-image">
              <img src="https://storage.googleapis.com/blah_blah_bucket/board/eee256ea-b2b4-4a4c-8fc9-e75c8c876a68_chunsik.png" alt="프로필 이미지" width="33" height="33">
            </div>
            <div class="user-details">
              <span class="name">Name</span>
              <p class="description">Description</p>
            </div>
          </div>
        </div>
        <div class="vote-card">
          <h3 class="vote-title">"여름 vs 겨울"</h3>
          <div class="user-info">
            <div class="profile-image">
              <img src="https://storage.googleapis.com/blah_blah_bucket/board/eee256ea-b2b4-4a4c-8fc9-e75c8c876a68_chunsik.png" alt="프로필 이미지" width="33" height="33">
            </div>
            <div class="user-details">
              <span class="name">Name</span>
              <p class="description">Description</p>
            </div>
          </div>
        </div>
        <div class="vote-card">
          <h3 class="vote-title">"여행지 추천"</h3>
          <div class="user-info">
            <div class="profile-image">
              <img src="https://storage.googleapis.com/blah_blah_bucket/board/eee256ea-b2b4-4a4c-8fc9-e75c8c876a68_chunsik.png" alt="프로필 이미지" width="33" height="33">
            </div>
            <div class="user-details">
              <span class="name">Name</span>
              <p class="description">Description</p>
            </div>
          </div>
        </div>
      </div>
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
  console.log("여기 확인해 데이터타입==>",url)
  
  try {
    await axios.post('http://localhost:80/board/hits', null, {
      params: { url },
      withCredentials: true
    });
    alert("여기 맞아?")
  } catch (err) {1
    console.error('조회수 증가 실패:', err);
  }
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
.vote-best-wrapper {
display: flex;
gap: 15px;
margin-top: 15px;
}

.vote-card {
flex: 1;
background-color: #f9f9f9;
border: 1px solid #eee;
border-radius: 5px;
padding: 15px;
text-align: center;
}

.vote-title {
font-size: 16px;
margin-top: 0;
margin-bottom: 10px;
}

.user-info {
display: flex;
align-items: center;
gap: 10px;
}

.profile-image {
width: 30px;
height: 30px;
border-radius: 50%;
overflow: hidden;
}

.profile-image img {
width: 100%;
height: 100%;
object-fit: cover;
}

.user-details {
text-align: left;
flex-grow: 1;
}

.user-details .name {
display: block;
font-weight: bold;
font-size: 14px;
}

.user-details .description {
font-size: 12px;
color: #777;
margin-top: 3px;
overflow: hidden;
text-overflow: ellipsis;
white-space: nowrap;
}
</style>