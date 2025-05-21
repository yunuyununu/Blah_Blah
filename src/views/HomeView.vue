<template>
<div class="content">
  <div class="container">
    <div class="row">
      <div class="col-6">
        <h2>게시글 주간 토픽 베스트</h2>
        <table class="my-table">
        <thead>
          <tr>
            <th>제목</th>
            <th>작성일</th>
            <th>조회수</th>
            <th>좋아요수</th>
          </tr>
        </thead>
        <tbody>
          <!-- <tr v-for="(person, index) in people" :key="index">
            <td>{{ person.name }}</td>
            <td>{{ person.age }}</td>
            <td>{{ person.job }}</td>
          </tr> -->
        </tbody>
      </table>
      </div>
      <div class="col-6">
        <h2>게시글 월간 토픽 베스트</h2>
      </div>
    </div>
    <div class="row">
      <h2>투표 베스트</h2>
    </div>
  </div>
</div>
</template>

<script setup>


</script>

<style scoped>
.container {
  padding: 16px;
}
h2 {
  font-size: 30px;
  font-weight: 700;
}
</style>