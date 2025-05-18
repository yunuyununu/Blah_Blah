<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col">
          <a>게시판 리스트</a>
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>제목</th>
                  <th>작성자</th>
                  <th>조회수</th>
                  <th>작성일</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in board" :key="item.b_idx">
                  <td>{{ item.b_idx }}</td>
                  <td>{{ item.b_title }}</td>
                  <td>{{ item.u_nicname }}</td>
                  <td>{{ item.b_hits }}</td>
                  <td>{{ item.b_date }}</td>
                </tr>
              </tbody>
            </table>

        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue';
  import axios from 'axios'

  const board = ref([]);
  
  onMounted(async () => {
    const response = await axios.get('http://localhost:80/board/list');
    console.log(response.data)
    board.value = response.data;
  });
  </script>
  
  <style scoped>
  table {
    width: 100%;
    border-collapse: collapse;
  }
  th, td {
    border: 1px solid #ddd;
    padding: 8px;
  }
  </style>