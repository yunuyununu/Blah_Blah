import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import MyInformation from '@/views/MyInformation.vue';
import MyReview from '@/views/MyReview.vue';
import MyPage from '@/views/MyPage.vue';
import CompanyVue from '@/views/CompanyVue.vue';
import CompanyList from '@/views/CompanyList.vue';
import CompanyChange from '@/views/CompanyChange.vue';
import BoardVue from '@/views/BoardVue.vue';
import BoardList from '@/views/BoardList.vue';
import BoardRegistration from '@/views/BoardRegistration.vue';
import UserJoin from '@/views/UserJoin.vue';

//라우트(routes) 정의 : URL 요청에 대해 어떤 페이지(컴포넌트)를 보여줄지에 대한 매핑정보를 정의
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/join',
    name: 'join',
    component: UserJoin
  },
  {
    path: '/mypage',
    name: 'mypage',
    component: MyPage,
    children: [
      {
        path: 'myinformation', // 기본 Router
        name: 'myinformation',
        component: MyInformation
      },
      {
        path: 'myreview',
        name: 'myreview',
        component: MyReview
      }
    ]
  },
  {
    path: '/company',
      component: CompanyVue,
      children: [
        {
          path: 'companylist',
          name: 'companylist',
          component: CompanyList,
        },
        {
          path: 'companychange',
          name: 'companychange',
          component: CompanyChange,
        },
      ],
  },
  {
    path: '/board',
      component: BoardVue,
      children: [
        {
          path: 'boardlist',
          name: 'boardlist',
          component: BoardList,
        },
        {
          path: 'boardregistration',
          name: 'boardregistration',
          component: BoardRegistration,
        },
      ],
  }
];
const router = createRouter({
  history: createWebHistory('/'),
  routes,
});

export default router;