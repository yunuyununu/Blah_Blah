<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col">
          <form @submit.prevent="submitPost">
            <input v-model="title" placeholder="제목" required/>
            <br><br>
            <textarea v-model="content" placeholder="내용" required></textarea>

            <input type="file" multiple @change="onImageChange" accept=".jpg,.jpeg,.png,.pdf"/>
            <div class="image-preview">
              <img v-for="(img, i) in previewImages" :key="i" :src="img" />
            </div>
            <br>
            <button class="btn btn-outline-dark" type="submit">게시글 등록</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

axios.defaults.withCredentials = true;
const title = ref('')
const content = ref('')
const files = ref([])
const previewImages = ref([])

const onImageChange = (e) => {
  files.value = Array.from(e.target.files)
  previewImages.value = files.value.map(file => URL.createObjectURL(file))
}

const submitPost = async () => {
  const formData = new FormData()
  formData.append('b_title', title.value)
  formData.append('b_content', content.value)
  files.value.forEach(file => formData.append('images', file))

  try {
    const res = await axios.post('http://localhost:80/board/boardInsert', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      withCredentials: true
    })

    if (res.data === 'success') {
      alert('게시글 등록 완료')
      router.push('/board/boardlist')
    } else {
      alert('게시글 등록 실패')
    }
  } catch (err) {
    console.error(err)
    alert('등록 중 오류 발생')
  }
}
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
  min-height: 300px;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  resize: vertical;
}

.image-preview {
  display: flex;
  flex-wrap: wrap;
  margin-top: 10px;
  gap: 10px;
}

.image-preview img {
  width: 100px;
  height: 100px;
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
input[type="text"],
input[type="file"],
input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
}
</style>
