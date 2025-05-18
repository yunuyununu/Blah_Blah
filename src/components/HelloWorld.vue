<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const users = ref([])

onMounted(async () => {
  const res = await axios.get('http://localhost:80/user')
  console.log(res.data)
  users.value = res.data
})
</script>

<template>
  <div>
    <h2>사용자 목록</h2>
    <ul>
      <li v-for="user in users" :key="user.id">
        {{ user.name }} ({{ user.createdAt }})
      </li>
    </ul>
  </div>
</template>
