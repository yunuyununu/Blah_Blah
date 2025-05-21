<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col">
          <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>회사명</th>
                  <th>업계</th>
                  <th>설립일자</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in company" :key="item.c_idx">
                  <td>{{ item.c_idx }}</td>
                  <td>{{ item.c_name }}</td>
                  <td>{{ item.c_industry }}</td>
                  <td>{{ item.c_est }}</td>
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

  const company = ref([]);
  
  onMounted(async () => {
    const response = await axios.get('http://localhost:80/company/list');
    console.log(response.data)
    company.value = response.data;
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