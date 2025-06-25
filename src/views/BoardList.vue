<template>
  <div class="board-container">
    <!-- 검색창 -->
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
      <button v-if="searchKeyword.trim() !== ''" @click="clearSearch" class="clear-btn">전체보기</button>
    </div>

    <!-- 게시글 카드 목록 -->
    <div v-if="board.length > 0" class="card-grid">
      <!-- 카드 -->
      <div class="card" v-for="item in board" :key="item.B_IDX" @click="goBoardDetails(item.B_IDX)">
        <div class="card-inner">
          <!-- 텍스트 영역 -->
          <div class="card-text">
            <div class="card-title">{{ item.B_TITLE }}</div>
            <div class="card-content" v-if="item.B_CONTENT">{{ item.B_CONTENT.slice(0, 80) }}...</div>
            <div class="card-info">
              <span>{{ item.C_NAME }} · {{ item.U_NICNAME }}</span>
              <span>♥ {{ item.heart_count }}</span>
              <span>
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-chat" viewBox="0 0 16 16">
                  <path d="M2.678 11.894a1 1 0 0 1 .287.801 11 11 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8 8 0 0 0 8 14c3.996 0 7-2.807 7-6s-3.004-6-7-6-7 2.808-7 6c0 1.468.617 2.83 1.678 3.894m-.493 3.905a22 22 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a10 10 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9 9 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105"/>
                </svg>&nbsp;{{ item.comment_count }}
              </span>
              <span>{{ formatDate(item.B_DATE) }}</span>
            </div>
          </div>

          <!-- 썸네일 이미지 영역 -->
          <div v-if="item.I_IMAGE" class="card-thumbnail">
            <img :src="item.I_IMAGE" alt="썸네일" />
          </div>
        </div>
      </div>
    </div>

    <!-- 페이지네이션 -->
    <div v-if="totalPages > 1" class="pagination">
      <button @click="changePage(1)" :disabled="currentPage === 1">처음</button>
      <button @click="changePage(currentPage - 1)" :disabled="currentPage === 1">이전</button>

      <button
        v-for="page in pageNumbers"
        :key="page"
        @click="changePage(page)"
        :class="{ active: currentPage === page }"
      >
        {{ page }}
      </button>

      <button @click="changePage(currentPage + 1)" :disabled="currentPage === totalPages">다음</button>
      <button @click="changePage(totalPages)" :disabled="currentPage === totalPages">마지막</button>
    </div>

    <!-- 검색결과 없을 때 메시지 -->
    <div v-if="board.length === 0" class="no-results">
      <p v-if="searchKeyword.trim() !== ''">검색결과가 없습니다!</p>
      <p v-else>게시글이 없습니다.</p>
      <p v-if="searchKeyword.trim() !== ''">검색할 단어를 변경하거나, 단어의 철자가 정확한지 확인해 보세요.</p>
    </div>

    <!-- 로딩 상태 -->
    <div v-if="loading" class="loading">
      <p>로딩중...</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const board = ref([]);
const currentPage = ref(1);
const totalPages = ref(1);
const totalCount = ref(0);
const pageSize = 12;
const searchKeyword = ref('');
const searchInput = ref(null);
const loading = ref(false);
const router = useRouter();

const formatDate = (datetime) => {
  return new Date(datetime).toLocaleDateString('ko-KR');
};

// 페이지 번호 배열 계산 (현재 페이지 기준으로 5개씩 보여주기)
const pageNumbers = computed(() => {
  const maxVisible = 5;
  const start = Math.max(1, currentPage.value - Math.floor(maxVisible / 2));
  const end = Math.min(totalPages.value, start + maxVisible - 1);
  const adjustedStart = Math.max(1, end - maxVisible + 1);
  
  return Array.from({ length: end - adjustedStart + 1 }, (_, i) => adjustedStart + i);
});

// 게시글 목록 조회
const fetchBoards = async (resetPage = false) => {
  try {
    loading.value = true;
    
    if (resetPage) {
      currentPage.value = 1;
    }

    const res = await axios.get('http://localhost:80/board/boards', {
      params: {
        searchKeyword: searchKeyword.value.trim(),
        page: currentPage.value,
        pageSize: pageSize,
      },
    });

    board.value = res.data.list || [];
    totalPages.value = res.data.totalPages || 1;
    totalCount.value = res.data.totalCount || 0;
    
  } catch (err) {
    console.error('게시글 조회 실패:', err);
    board.value = [];
    totalPages.value = 1;
    totalCount.value = 0;
  } finally {
    loading.value = false;
  }
};

// 페이지 변경
const changePage = async (page) => {
  if (page < 1 || page > totalPages.value || page === currentPage.value) return;
  currentPage.value = page;
  await fetchBoards();
  
  // 페이지 변경 시 스크롤을 맨 위로
  //window.scrollTo({ top: 0, behavior: 'smooth' });
};

// 검색
const onSearch = async () => {
    if (searchKeyword.value.trim() === '') {
    searchInput.value.focus(); // 입력창에 포커스 주기
    return; // 검색 호출 중단
  }
  await fetchBoards(true);
};

// 검색 초기화
const clearSearch = async () => {
  searchKeyword.value = '';
  await fetchBoards(true);
};

// 게시글 상세 이동
const goBoardDetails = async (b_idx) => {
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

onMounted(() => {
  fetchBoards();
});
</script>
<style scoped>
.board-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.search-container {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  align-items: center;
}

.search-input {
  flex: 1;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.search-container button {
  padding: 10px 20px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.clear-btn {
  background: #6c757d !important;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 0;
  cursor: pointer;
  transition: box-shadow 0.2s;
  overflow: hidden;
}

.card:hover {
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.card-title {
  font-weight: bold;
  font-size: 16px;
  margin-bottom: 10px;
}

.card-content {
  color: #666;
  margin-bottom: 15px;
  line-height: 1.4;
}

.card-info {
  display: flex;
  flex-wrap: wrap;  /* 넘치면 줄바꿈 */
  gap: 6px 10px;     /* 줄과 요소 간 간격 */
  font-size: 12px;
  color: #888;
  margin-top: auto; /* 항상 맨 아래로 */
}

.card-info span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.card-inner {
  display: flex;
  flex-direction: row;
  align-items: stretch;
  padding: 20px;
  gap: 10px;
  height: 100%;
}

.card-text {
  flex: 1;
  display: flex;
  flex-direction: column;
  /* justify-content: space-between; */
}

.card-thumbnail {
  width: 80px;
  height: 80px;
  flex-shrink: 0;
  border-radius: 8px;
  overflow: hidden;
}

.card-thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.pagination {
  display: flex;
  justify-content: center;
  gap: 5px;
  margin: 20px 0;
}

.pagination button {
  padding: 8px 12px;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
  border-radius: 4px;
}

.pagination button:hover:not(:disabled) {
  background: #f5f5f5;
}

.pagination button.active {
  background: #007bff;
  color: white;
  border-color: #007bff;
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.no-results, .loading {
  text-align: center;
  padding: 40px;
  color: #666;
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