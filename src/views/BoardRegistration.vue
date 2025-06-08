<template>
  <div class="content">
    <div class="container">
      <div class="row">
        <div class="col">

          <!-- 게시물 유형 선택 -->
          <div class="mb-3">
            <button type="button" class="btn btn-outline-primary me-2" @click="isVotePost = false">일반 게시물</button>
            <button type="button" class="btn btn-outline-success" @click="isVotePost = true">투표 게시물</button>
          </div>

          <form @submit.prevent="submitPost">
            <input v-model="title" ref="titleInput" placeholder="제목을 입력하세요. (50자 이내)"
            :class="['custom-input', { 'input-error': errors.title  }]"/>
            <p v-if="errors.title" class="error-text">{{ errors.title }}</p>
            <br><br>
            <textarea
              v-model="content"
              ref="contentInput"
              placeholder="내용을 입력하세요."
              :class="['custom-input', { 'input-error': errors.content  }]"
            ></textarea>
            <p class="text-muted text-end">{{ content.length }}자</p>
            <p v-if="errors.content" class="error-text">{{ errors.content }}</p>
            <br>

            <!-- 이미지 업로드 -->
            <input type="file" multiple @change="onImageChange"  accept=".jpg,.jpeg,.png,.pdf" />
            <p>※ 첨부 가능한 파일은 JPG, JPEG, PNG, PDF 형식만 가능합니다.</p>
            <div class="image-preview">
              <img v-for="(img, i) in previewImages" :key="i" :src="img" />
            </div>

            <!-- 투표 게시물일 경우 추가 입력란 -->
            <div v-if="isVotePost" class="mt-4">
              <input v-model="voteTitle" placeholder="투표 제목을 입력하세요." ref="voteTitleInput" class="custom-input vote-title-input"
              :class="['custom-input', { 'input-error': errors.voteTitle  }]"/>
              <p v-if="errors.voteTitle" class="error-text">{{ errors.voteTitle }}</p>
              <br>

              <div v-for="(option, index) in voteOptions" :key="index" class="d-flex mb-2" >
                <input
                  v-model="voteOptions[index]"
                  placeholder="항목 입력"
                  class="custom-input vote-option-input"
                  :ref="el => voteOptionRefs[index] = el"
                  :class="['custom-input', { 'input-error': errors.voteOptions[index] }]"
                />
                <p v-if="errors.voteOptions[index]" class="error-text">{{ errors.voteOptions[index] }}</p>
                <button type="button" @click="removeOption(index)" class="btn btn-sm btn-danger ms-2">삭제</button>
              </div>
              <button type="button" class="btn btn-sm btn-outline-secondary" @click="addOption">항목 추가</button>
            </div>

            <br>
            <button class="btn btn-outline-dark mt-3" type="submit">게시글 등록</button>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref,watch } from 'vue'
import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

axios.defaults.withCredentials = true

// 공통 게시글 정보
const title = ref('')
const content = ref('')
const files = ref([])
const previewImages = ref([])
const isVotePost = ref(false) // false = 일반 게시물, true = 투표 게시물

const titleInput = ref(null)
const contentInput = ref(null)

const errors = ref({
      title : '',
      content : '',
      voteTitle : '',
      voteOptions : ['','']
    })

// 투표 관련
const voteTitle = ref('')
const voteOptions = ref(['', '']) // 기본 2개 항목
const voteOptionRefs = ref([])

const voteTitleInput = ref('')

const onImageChange = (e) => {
  files.value = Array.from(e.target.files)
  previewImages.value.forEach(url => URL.revokeObjectURL(url))
  previewImages.value = files.value.map(file => URL.createObjectURL(file))
}

const addOption = () => {
  voteOptions.value.push('')
  errors.value.voteOptions.push('')
}
const removeOption = (index) => {
  voteOptions.value.splice(index, 1)
  errors.value.voteOptions.splice(index, 1)
}

const submitPost = async () => {

   let isValid = true

  // 미입력 시 에러메세지
  if (!title.value) {
    errors.value.title = '제목을 입력하세요.'
    if (isValid) titleInput.value.focus()
    return
  }
  if (!content.value) {
    errors.value.content = '내용을 입력하세요.'
    if (isValid) contentInput.value.focus()
    return
  }

  const formData = new FormData()
  formData.append('b_title', title.value)
  formData.append('b_content', content.value)
  // formData.append('is_vote', isVotePost.value) // 백엔드에서 이걸로 구분

  files.value.forEach(file => formData.append('images', file))

  // 투표 게시물인 경우 추가 정보 전송
  // if (isVotePost.value) {
  //   formData.append('v_title', voteTitle.value)
  //   voteOptions.value.forEach(option => formData.append('vote_options', option))
  // }

  if (isVotePost.value) {
    if (!voteTitle.value.trim()) {
      errors.value.voteTitle = '제목을 입력하세요.'
      voteTitleInput.value.focus()
      return
    }

    let hasError = false
    errors.value.voteOptions = voteOptions.value.map((option) => {
      if (!option.trim()) {
        hasError = true
        return '항목을 입력하세요.'
      }
      return ''
    })

    if (hasError) {
      const firstInvalidIndex = voteOptions.value.findIndex(option => !option.trim())
      voteOptionRefs.value[firstInvalidIndex]?.focus()
      return
    }
  } 


  try {
    const boardRes = await axios.post('http://localhost:80/board/boardInsert', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },withCredentials: true
    })
     if (typeof boardRes.data === 'object' && boardRes.data.result === 'success') {
      const b_idx = boardRes.data.b_idx

      // 투표 게시물일 경우 vote insert 추가
      if (isVotePost.value) {

        const voteForm = new FormData()
        voteForm.append('v_b_idx', b_idx)
        voteForm.append('v_title', voteTitle.value)
        voteOptions.value.forEach(option => voteForm.append('options', option))

        await axios.post('http://localhost:80/board/voteInfoInsert', voteForm)
      }

      alert('등록 완료')
      router.push('/board/boardlist')
    } else {
      alert('등록 실패')
    }

  } catch (err) {
    console.error(err)
    alert('등록 중 오류 발생')
  }
}

watch(title, () => {
    errors.value.title = ''
  })
watch(content, () => {
  errors.value.content = ''
})
watch(voteTitle, () => {
    errors.value.voteTitle = ''
  })
watch(voteOptions, (newOptions) => {
  errors.value.voteOptions = newOptions.map(() => '')
})
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
.custom-input::placeholder {
  font-size: 12px;   /* 크기 */
  color: #a3a2a2;       /* 색상 */
  font-style: italic; /* 스타일 */
}
.input-error {
  border: 2px solid red !important;
}
.error-text {
  color: red;
  font-size: 12px;
  margin-top: 3px;
  margin-bottom: 8px;
}
input[disabled] {
  background-color: #e9ecef;
  color: #6c757d;
  cursor: not-allowed;
}
.vote-option-input {
  width: 80%; /* 또는 max-width: 500px; 등 */
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.vote-title-input {
  margin-bottom: 16px;
}
</style>
