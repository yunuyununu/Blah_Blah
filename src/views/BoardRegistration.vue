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

            <input type="file" multiple @change="handleFileChange" class="custom-file-input"/>
            <div v-if="previewUrls.length" class="image-preview">
              <div v-for="(url, idx) in previewUrls" :key="idx">
                <img :src="url" alt="preview" width="100" @click="removeImage(idx)" />
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

  // 기존 URL 해제
  previewUrls.value.forEach((url) => URL.revokeObjectURL(url));

  // 새로운 미리보기 생성
  previewUrls.value = files.map(file => URL.createObjectURL(file));
};

const removeImage = (index) => {
  // 미리보기 URL 해제
  URL.revokeObjectURL(previewUrls.value[index]);

  // 해당 인덱스의 파일 및 URL 제거
  imageFiles.value.splice(index, 1);
  previewUrls.value.splice(index, 1);
};

const submitPost = () => {
  // 여기에 폼 제출 로직 추가
  console.log('제목:', title.value);
  console.log('내용:', content.value);
  console.log('이미지 파일:', imageFiles.value);
};
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
  margin-right: 10px;
  max-width: 100px;
  height: auto;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
  transition: 0.3s ease;
}
.image-preview img:hover {
  opacity: 0.7;
}
label {
  font-weight: bold;
  margin-bottom: 6px;
}
</style>
