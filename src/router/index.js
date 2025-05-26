import { createRouter, createWebHistory } from 'vue-router';

//라우트(routes) 정의 : URL 요청에 대해 어떤 페이지(컴포넌트)를 보여줄지에 대한 매핑정보를 정의
const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/HomeView')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/UserLogin')
  },
  {
    path: '/searchid',
    name: 'searchid',
    component: () => import('@/views/SearchId')
  },
  {
    path: '/searchpw',
    name: 'searchpw',
    component: () => import('@/views/SearchPw')
  },
  {
    path: '/passwdreset',
    name: 'passwdreset',
    component: () => import('@/views/PasswdReset')
  },
  {
    path: '/join',
    name: 'join',
    component: () => import('@/views/UserJoin')
  },
  {
    path: '/mypage',
    component: () => import('@/views/MyPage.vue'),
    children: [
      {
        path: 'myinformation', // 기본 Router
        name: 'myinformation',
        component: () => import('@/views/MyInformation')
      },
      {
        path: 'myreview',
        name: 'myreview',
        component: () => import('@/views/MyReview')
      }
    ]
  },
  {
    path: '/company',
      component: () => import('@/views/CompanyVue'),
      children: [
        {
          path: 'companylist',
          name: 'companylist',
          component: () => import('@/views/CompanyList'),
        },
        {
          path: 'companyInsert',
          name: 'companyInsert',
          component: () => import('@/views/CompanyInsert'),
        },
        {
          path: '/companydetails/:c_idx',
          name: 'companydetails',
          component: () => import('@/views/CompanyDetails')
        },
      ],
  },
  {
    path: '/board',
      component: () => import('@/views/BoardVue'),
      children: [
        {
          path: 'boardlist',
          name: 'boardlist',
          component: () => import('@/views/BoardList'),
        },
        {
          path: 'boardregistration',
          name: 'boardregistration',
          component: () => import('@/views/BoardRegistration'),
        },
        {
          path: '/boarddetails/:b_idx',
          name: 'boarddetails',
          component: () => import('@/views/BoardDetails')
        },
      ],
  }

];
const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;