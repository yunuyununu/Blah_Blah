<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col">
          <div class="post-form">
            <div class="form-group">
              <label>제목</label>
              <input type="text" v-model="title" placeholder="제목을 입력하세요" />
            </div>

            <div class="form-group">
              <label>내용</label>
              <textarea v-model="content" placeholder="내용을 입력하세요"></textarea>
            </div>

            <input type="file" multiple @change="handleFileChange" />
              <div v-if="previewUrls.length">
                <div v-for="(url, idx) in previewUrls" :key="idx">
                  <img :src="url" alt="preview" width="100" />
                </div>
              </div>
            <div style="text-align: center;">
              <button @click="submitPost" class="btn btn-dark">등록</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  </template>
  
  <script setup>
import { ref } from 'vue';

const title = ref('');
const content = ref('');
const imageFiles = ref([]);
const previewUrls = ref([]);

const handleFileChange = (e) => {
  const files = Array.from(e.target.files);
  imageFiles.value = files;

  // 미리보기 생성
  previewUrls.value = files.map(file => URL.createObjectURL(file));
};

// const submitPost = () => {
//   if (!title.value || !content.value) {
//     alert('제목과 내용을 모두 입력해주세요.');
//     return;
//   }

//   const formData = new FormData();
//   formData.append('title', title.value);
//   formData.append('content', content.value);
//   if (imageFile.value) {
//     formData.append('image', imageFile.value);
//   }

//   // 예: 게시글 등록 API 호출
//   fetch('/api/posts', {
//     method: 'POST',
//     body: formData,
//   })
//     .then(res => res.json())
//     .then(data => {
//       alert('게시글이 등록되었습니다.');
//       // 입력 초기화
//       title.value = '';
//       content.value = '';
//       imageFile.value = null;
//     })
//     .catch(err => {
//       console.error(err);
//       alert('게시글 등록에 실패했습니다.');
//     });
// };
</script>

<style scoped>
.post-form {
  max-width: 600px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 16px;
}

input[type="text"],
textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.image-preview img {
  margin-top: 10px;
  max-width: 100%;
  height: auto;
  border: 1px solid #ccc;
  border-radius: 4px;
}
label {
  font-weight: bold;
  margin-bottom: 6px;
}
</style>