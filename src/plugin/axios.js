// import axios from 'axios';
// import router from '@/router';

// 로그인 체크 -> 401 처리


// const api = axios.create({
//   baseURL: 'http://localhost:80', // API 서버 주소
//   withCredentials: true, // 세션 쿠키 포함
// });

// // 응답 인터셉터 설정
// api.interceptors.response.use(
//   function (response) {
//     // 2xx 범위에 있는 상태 코드는 이 함수를 트리거
//     // 응답 데이터가 있는 작업 수행
//     return response;
//   },
//   function (error) {
//     if (error.response.status === 401) {
//       console.log('401 detected, redirecting...');
//       router.push('/login').catch(() => {});
//     } else {
//       return Promise.reject(error);
//     }
//     return Promise.reject(error);

//   }
// );

// export default api;