<template>
  <div class="board-container">
    <div class="card-grid">
      <div class="card" v-for="item in board" :key="item.b_idx" @click="goBoardDetails(item.b_idx)">
        <div class="card-title">{{ item.b_title }}</div>
        <div class="card-content">{{ item.b_content.slice(0, 80) }}...</div>
        <div class="card-info">
          <a>{{ item.c_name }} · {{ item.u_nicname }}</a>
          <span>♥ ♡</span>
          <span>👁 {{ item.b_hits }}</span>
          <span>{{ formatDate(item.b_date) }}</span>
        </div>
      </div>
    </div>
    <button class="load-more" v-if="hasMore" @click="fetchBoards">더보기</button>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router'
import axios from 'axios';

const router = useRouter()

const board = ref([]);
const lastBIdx = ref(null);
const hasMore = ref(true);

const fetchBoards = async () => {
  const res = await axios.get('http://localhost:80/board/boards', {
    params: {
      lastBIdx: lastBIdx.value,
    },
  })

  const newPosts = res.data;
  if (newPosts.length < 12) hasMore.value = false;

  if (newPosts.length > 0) {
    board.value.push(...newPosts);
    lastBIdx.value = newPosts[newPosts.length - 1].b_idx;
  }
};

const formatDate = (datetime) => {
  return new Date(datetime).toLocaleDateString();
};

onMounted(() => {
  fetchBoards();
});

const goBoardDetails = async (b_idx) => {
  try {
    await axios.post('http://localhost:80/board/hits', null, {
      params: { b_idx },
      withCredentials: true // 쿠키를 서버로 보내기 위해 필요
    });
  } catch (err) {
    console.error('조회수 증가 실패:', err);
  }
  router.push({ name: 'boarddetails', params: { b_idx } });
};
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
</style>
