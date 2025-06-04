<template>
  <div class="board-container">
    <div class="search-container">
      <input
        v-model="searchKeyword"
        type="text"
        placeholder="게시글 제목 또는 내용을 입력하세요"
        @keyup.enter="onSearch"
        class="search-input"
         ref="searchInput"
      />
      <button @click="onSearch">검색</button>
    </div>
    <div class="card-grid">
      <div class="card" v-for="item in board" :key="item.b_idx" @click="goBoardDetails(item.b_idx)">
        <div class="card-title">{{ item.b_title }}</div>
        <div class="card-content">{{ item.b_content.slice(0, 80) }}...</div>
        <div class="card-info">
          <a>{{ item.c_name }} · {{ item.u_nicname }}</a>
          <span>♥ {{ item.heart_count }}</span>
          <span><svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-eye" viewBox="0 0 16 16">
  <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8M1.173 8a13 13 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5s3.879 1.168 5.168 2.457A13 13 0 0 1 14.828 8q-.086.13-.195.288c-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5s-3.879-1.168-5.168-2.457A13 13 0 0 1 1.172 8z"/>
  <path d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5M4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0"/>
</svg>&nbsp;{{ item.b_hits }}</span>
          <span>{{ formatDate(item.b_date) }}</span>
        </div>
      </div>
    </div>
    <!-- 검색결과 없을 때 메시지 -->
    <div v-if="board.length === 0 && searchKeyword.trim() !== ''" class="no-results">
      <p>검색결과가 없습니다!</p>
      <p>검색할 단어를 변경하거나, 단어의 철자가 정확한지 확인해 보세요.</p>
    </div>
    <!-- 무한스크롤 감지용 div -->
    <div ref="infiniteScrollTrigger" class="scroll-trigger" v-show="hasMore"></div>
  </div>
</template>

<script setup>
import { onMounted, ref, onBeforeUnmount, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const formatDate = (datetime) => {
  return new Date(datetime).toLocaleDateString();
};
const router = useRouter();

const board = ref([]);
const lastBIdx = ref(null);
const hasMore = ref(true);
const infiniteScrollTrigger = ref(null);
let observer;

const searchKeyword = ref('');  // 검색어 상태
const searchInput = ref(null);

const savedY = parseInt(sessionStorage.getItem('boardScrollY')) || 0;
let isRestoringScroll = savedY > 0;

// 게시글 목록 가져오기 함수
const fetchBoards = async (isSearch = false) => {
  if (!hasMore.value) return;

  // 검색이면 리스트 초기화 + lastBIdx 초기화
  if (isSearch) {
    board.value = [];
    lastBIdx.value = null;
    hasMore.value = true;
  }

  const res = await axios.get('http://localhost:80/board/boards', {
    params: { 
      lastBIdx: lastBIdx.value,
      searchKeyword: searchKeyword.value.trim()  // 검색어 같이 전달
    },
  });

  const newPosts = res.data;
  if (newPosts.length < 12) hasMore.value = false;

  if (newPosts.length > 0) {
    board.value.push(...newPosts);
    lastBIdx.value = newPosts[newPosts.length - 1].b_idx;
  }

  if (isRestoringScroll) {
  await nextTick();
  const waitUntilScrollable = async (targetY) => {
    return new Promise((resolve) => {
      const interval = setInterval(async () => {
        if (document.documentElement.scrollHeight >= targetY + window.innerHeight) {
          clearInterval(interval);
          resolve();
        } else {
          if (hasMore.value) {
            await fetchBoards();
          }
        }
      }, 30);
    });
  };
  await waitUntilScrollable(savedY);

  // 바로 scrollTo 하고, scroll 잠금 해제
  window.scrollTo({ top: savedY, behavior: 'auto' });

  sessionStorage.removeItem('boardScrollY');
  isRestoringScroll = false;

  // 복원 후 스크롤 다시 허용
  document.body.style.overflow = '';
}
};

// 검색 버튼 클릭 또는 Enter 키 이벤트
const onSearch = async () => {
  if (searchKeyword.value.trim() === '') {
    searchInput.value?.focus(); // ✅ 검색어가 없으면 input에 포커스
    return;
  }
  hasMore.value = true;
  await fetchBoards(true);
};

const goBoardDetails = async (b_idx) => {
  sessionStorage.setItem('boardScrollY', window.scrollY);
  try {
    await axios.post('http://localhost:80/board/hits', null, {
      params: { b_idx },
      withCredentials: true
    });
  } catch (err) {
    console.error('조회수 증가 실패:', err);
  }
  router.push({ name: 'boarddetails', params: { b_idx } });
};

const createObserver = () => {
  observer = new IntersectionObserver((entries) => {
    if (entries[0].isIntersecting && hasMore.value && !isRestoringScroll) {
      fetchBoards();
    }
  });

  if (infiniteScrollTrigger.value) {
    observer.observe(infiniteScrollTrigger.value);
  }
};

onMounted(async () => {
  if (isRestoringScroll) {
    document.body.style.overflow = 'hidden'; // 스크롤 잠금
  }

  await fetchBoards();
  createObserver();
});

onBeforeUnmount(() => {
  if (observer && infiniteScrollTrigger.value) {
    observer.unobserve(infiniteScrollTrigger.value);
  }
});
</script>


<style scoped>
.board-container {
  max-width: 1000px;
  margin: auto;
  padding: 20px;
}
.search-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
  gap: 8px;
}
.search-container button {
  padding: 8px 16px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}
.search-input {
  width: 400px;
  padding: 8px 12px;
  font-size: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
}
.search-bar input {
  flex-grow: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px 0 0 6px;
  font-size: 1rem;
}

.search-bar button {
  padding: 8px 16px;
  border: none;
  background-color: #f44336;
  color: white;
  font-weight: bold;
  cursor: pointer;
  border-radius: 0 6px 6px 0;
  transition: background-color 0.3s ease;
}

.search-bar button:hover {
  background-color: #d32f2f;
}
h2 {
  margin-bottom: 16px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(47%, 1fr));
  gap: 16px;
}

.card {
  background: white;
  border: 1px solid #ddd;
  border-radius: 10px;
  padding: 16px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.card-title {
  font-weight: bold;
  font-size: 1.1rem;
  margin-bottom: 8px;
}

.card-content {
  font-size: 0.95rem;
  color: #444;
  margin-bottom: 12px;
}

.card-info {
  display: flex;
  justify-content: space-between;
  font-size: 0.85rem;
  color: #888;
}

.load-more {
  display: block;
  margin: 24px auto 0;
  padding: 10px 20px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
}
.scroll-trigger {
  height: 1px;
}
.no-results {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  
  height: 300px; /* 적당한 높이 지정 */
  color: #999;
  font-size: 1.2rem;
  white-space: pre-line; /* 줄바꿈 유지 */
  user-select: none;
  text-align: center;
  padding: 20px;
}
.no-results p {
  margin: 8px 0;
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
