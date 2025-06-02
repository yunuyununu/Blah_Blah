<template>
  <div class="board-container">
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

const savedY = parseInt(sessionStorage.getItem('boardScrollY')) || 0;
let isRestoringScroll = savedY > 0;

const fetchBoards = async () => {
  if (!hasMore.value) return;

  const res = await axios.get('http://localhost:80/board/boards', {
    params: { lastBIdx: lastBIdx.value },
  });

  const newPosts = res.data;
  if (newPosts.length < 12) hasMore.value = false;

  if (newPosts.length > 0) {
    board.value.push(...newPosts);
    lastBIdx.value = newPosts[newPosts.length - 1].b_idx;
  }

  // 스크롤 복원이 필요한 경우, 충분히 로드될 때까지 반복 호출
  if (isRestoringScroll) {
    await nextTick(); // 렌더링 기다림
    const waitUntilScrollable = async (targetY) => {
        return new Promise((resolve) => {
          const interval = setInterval(async () => {
            if (document.documentElement.scrollHeight >= targetY + window.innerHeight) {
              clearInterval(interval);
              resolve();
            } else {
              if (hasMore.value) {
                await fetchBoards(); // 계속 불러오기
              }
            }
          }, 100); // 0.1초마다 체크
        });
    };
    await waitUntilScrollable(savedY);
    window.scrollTo({ top: savedY, behavior: 'auto' }); // 바로 이동
    sessionStorage.removeItem('boardScrollY');
    isRestoringScroll = false;
  }
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
</style>
