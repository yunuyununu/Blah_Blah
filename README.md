# 📚 Blah Blah
## 회사 익명 사이트

본 프로젝트는 회사 OJT 기간에 학습한 내용을 바탕으로 진행된 1인 프로젝트입니다.


* * *

## 📑 목차

[__1. 프로젝트 개요__](#-프로젝트-개요)
   - [주제 및 목표](#-주제-및-목표)
   - [산출물 및 사용기술](#-산출물-및-사용기술)

  
[__2. 주요 구현기능 설명__](#-주요-구현기능-설명)
   - [회원 회원가입/로그인](#-회원-회원가입/로그인)
   - [마이페이지/리뷰](#-마이페이지/리뷰)
   - [게시판](#-게시판)
   - [회사](#-회사)
   - [알림](#-알림)

    
[__3. 프로젝트 후기 및 발표자료__](#-후기)


🔗 

* * *

## 📌 프로젝트 개요

#### 📅 기간
2025.05.07 ~ 2025.06.13 (약 5주)

- 기획 : 기획 배경, 주제 선정, 요구사항 정의, 일정 수립, 수행 계획, WBS 작성
- 분석 : 상세기능 정의, 기술 검토, 프로그램 목록, 오브젝트 정의
- 설계 : 화면 설계, 서버 설계, DB 설계
- 구현 : 서버 구축, 웹 개발
- 테스트 : 웹 테스트, 테스트 결과서
- 종료 : 완료 보고서. 6/13 최종 시연.



#### 🖍 주제 및 목표

__[주제]__  회사 익명 커뮤니티

__[목표]__
1. 사이트 측면 : 직장 내 다양한 이슈를 실명 부담 없이 공유 가능하기 위함
2. 프로젝트 측면 : OJT 기간에 학습한 내용을 활용하기 위함


#### 👥 산출물 및 사용기술


__[산출물]__

* 기획
1) 요구사항 정의 - 요구사항 기술서
2) 수행계획 작성 - 프로젝트 수행계획서
3) 일정 계획 - 프로젝트 전체 일정 계획(WBS)

* 분석/설계
1) UI, 페이지구성 등 사이트 전체 설계도 - 스토리보드
2) DB 설계 - 시퀀스 다이어그램, 클래스 다이어그램, ERD
3) 프로젝트 - 프로그램 사양서
4) 오브젝트 - 테이블 사양서
5) 프로그램 - 프로그램 목록

* 구현
웹개발 - 소스파일

* 테스트
테스트 - 테스트 결과서

* 완료
완료 - 완료 보고서


__[사용기술 및 개발환경]__
- OS : Windows11
- Tools  :  Spring Tool Suite 4, MySQL Workbench 8.0, Git, GitHub, Visual Studio Code, ERDCloud
- Front-end  :  HTML/CSS, Javascript, Vue3.2.13, jsp
- Back-end  :  Java(JDK17),Spring Framework, Mysql 5.7.39, pandora3
- Library  :  MyBatis 1.4.2, Bootstrap 5.3.6 etc.

#### 👥 메뉴구조도 및 WBS, ERD

* 메뉴구조도
  
![메뉴구조도](https://github.com/user-attachments/assets/8e26e26c-9f81-476e-9da9-d4f4f0d591c7)

- FO : 홈, 마이페이지, 회사, 게시판
- BO : 회원관리, 회사관리, 리뷰관리, 게시판관리

* WBS
  
![WBS](https://github.com/user-attachments/assets/10ef159f-fd05-4fd1-88c2-51016d7c6c94)


* ERD
  
![BlahBlah](https://github.com/user-attachments/assets/597b6be6-c72a-4d74-ba53-d0601de36776)

총 12개 테이블

* * *


## 🕹️ 주요 구현기능 설명

#### 👀 회원 회원가입/로그인


 * 로그인 화면

![image](https://github.com/user-attachments/assets/8cbe8f03-ebdb-47ff-a738-15d8aed6ed9f)

- DB와 아이디, 비밀번호 일치 시 로그인 성공
- 탈퇴한 회원 로그인 시 알림창 (로그인 불가)
- 로그인 시 sse 연결되어 서버와 연결됨. (알림 전송을 위해)


* 아이디 찾기

![image](https://github.com/user-attachments/assets/d0e6e90b-f27e-41c2-b5c3-0456defc293f)

- DB에 저장된 이메일과 전화번호 일치 시 회원 아이디 앞 4자리 노출


* 비밀번호 찾기

1) 이메일, 아이디 검증

![image](https://github.com/user-attachments/assets/350d1377-9707-4f58-b8b6-8afbc66d4b62)

- 아이디, DB에 저장된 이메일 검증 후 인증번호 일치 시 성공 문구 및 비밀번호 재설정 버튼 활성화

2) 비밀번호 재설정

![image](https://github.com/user-attachments/assets/b17793b7-394a-426b-886d-457754abb9ca)

- 새로운 비밀번호 2곳 입력 후 재설정

* 회원가입
![image](https://github.com/user-attachments/assets/d686fa8b-9142-4549-8644-0f19ac08b4bf)

- 아이디 : 중복 체크 후 사용 가능 여부 체크
- 비밀번호 : 2곳 일치하는 비밀번호 입력
- 닉네임 : 3자 이상 입력 (중복 허용)
- 전화번호 : 자동하이픈
- 이메일 : 이메일 중복 확인 후 인증번호 전송됨. 영어 및 숫자 랜덤조합으로 6자리 인증번호 전송.
- 인증번호 : 전송된 인증코드 3분 이내로 인증가능. redis 서버에 3분동안 보관되어 시간초과 및 인증코드 검증완료 시 자동 삭제됨.
- 회사인증파일 : 파일 확장자 제한. 이미지파일 gcs의 회사인증파일 폴더에 자동 저장됨.



#### ✨ 마이페이지/리뷰


* 회원 정보
![image](https://github.com/user-attachments/assets/bbb217c0-ee01-4a16-8c0d-627887719682)

- 회원의 모든 정보 조회 페이지
- 닉네임, 전화번호 자유롭게 변경 가능
- 비밀번호 변경 페이지 분리
- 회원 탈퇴시 한번 더 확인알람 띄운 후 탈퇴 처리. (논리삭제)
- 회원가입 초기에 관리자의 회원인증 필요함
  
* 나의 리뷰
![image](https://github.com/user-attachments/assets/8c5304ad-80df-47e9-a52d-bccc45635e24)

- 회원의 회사인증 후 본인 회사 리뷰 가능. (리뷰 작성 버튼 활성화 됨)
- 리뷰내역 날짜 내림차순 정렬

* 리뷰 작성/수정 모달
![image](https://github.com/user-attachments/assets/dd7cc9ad-697c-4f6a-966d-b9dab404dd9b)
![image](https://github.com/user-attachments/assets/4ba440e7-043a-4792-ab96-22d14ec7f857)

- 리뷰 작성 후 30일 동안 수정 가능. 삭제는 언제든 가능.
- 리뷰 수정 후 바로 화면에 반영
- 회원 회사 인증 후 한 회사 당 1건의 리뷰만 등록 가능. (회원번호+회사번호 -> unique 설정)

* 회사 변경 신청
![image](https://github.com/user-attachments/assets/0e0e5e7c-695a-4632-9d87-630638857b32)

- 현재 회사 자동 입력
- 변경 할 회사 인증파일 1건 입력. 파일 확장자 제한됨.
- 비밀번호로 확인 후 회사 변경 신청 완료

* 이메일 변경
![image](https://github.com/user-attachments/assets/e1e3e1fa-9532-446f-89c1-ddeef2e06a8a)

- 바꿀 이메일 인증번호 검증 후 변경됨
- 회원가입 시 로직과 동일.



#### 📋 게시판


* 게시판 목록
![image](https://github.com/user-attachments/assets/66592490-0feb-4057-98b4-c5ed57a345f6)


- 게시글 최신순 정렬
- 한페이지에 12개씩 정렬됨.
- 처음, 마지막, 이전, 다음 버튼으로 페이지 이동
- 게시글 제목, 내용으로 검색
- 이미지 첨부파일 있으면 썸네일로 띄움

* 게시판 상세
- 게시판 상세 전체
![image](https://github.com/user-attachments/assets/5813d7b0-8cf8-4878-8856-8e2e9573b509)

- 게시글 클릭 시 redis 서버에 게시글번호+회원번호 키 30분동안 저장되어 조회수 1 증가.
- 30분 안에 재조회시 동일한 키가 입력되어 있으므로 조회수 변동 없음.
- 글 좋아요 클릭 시 게시글 작성자에게 알림. (sse를 사용한 실시간 전송 구현)
- 투표 게시물 -> 1인 1회 가능.
- 좋아요 클릭, 투표 참여 중복 불가. (게시글번호+회원번호/투표항목+회원번호 -> unique 설정)


- 투표 참여 후
![image](https://github.com/user-attachments/assets/363ca29b-ac6a-4cdc-88f1-ab9fb5fcb5c2)


- 댓글/대댓글
![image](https://github.com/user-attachments/assets/ea44b12f-da9b-45fb-96eb-ac1e7eb0bc58)

- 로그인 후 댓글/대댓글 작성 가능
- 게시글 작성자 댓글 작성 시 (작성자) 표시
- 댓글/대댓글 수정 삭제 가능

* 게시글 등록

- 일반 게시글 등록
![image](https://github.com/user-attachments/assets/79d4f231-5ebb-4550-a7a0-ba06051e728b)

- 일반 게시물 -> 제목, 내용, 이미지(10장까지) 
- 게시물 이미지 첨부 시 gcs에 저장됨.

- 투표 기능 추가
![image](https://github.com/user-attachments/assets/73ee5b36-2d7a-4a0e-a174-6d054d6efffb)

- 투표 제목, 항목 2가지 필수 입력.
- 항목 10개까지 추가 가능


#### 🏢 회사

* 회사 목록
![image](https://github.com/user-attachments/assets/aeb02356-ddb7-4a65-8026-13e79079c27d)
![image](https://github.com/user-attachments/assets/5701af70-cd18-4ea7-b291-108095584bae)

- 회사명, 업계, 설립일, 로고가 적힌 회사리스트
- 회사명으로 검색
- 처음, 마지막, 이전, 다음 버튼으로 페이지 이동
- 

* 회사 상세
![image](https://github.com/user-attachments/assets/1dfed2ca-6d6f-473f-8125-4a0611251d52)
![image](https://github.com/user-attachments/assets/f1e864fc-6dee-4cd6-9c96-1ac8fe5557b4)

- 회사 정보, 리뷰테이블에 저장된 별점이 계산된 평점(리뷰갯수 포함) 조회 가능
- 비로그인 및 비인증 회원은 회사리뷰 열람 불가능

* 회사 신청
![image](https://github.com/user-attachments/assets/dbaa9bc5-3e16-4ce1-bddb-b53e3f1aec2e)

- 회사명, 회사 대표 사진, 회사 소개, 설립일자, 사업자등록번호 입력 후 회사 신청


#### 🔔 알림 및 메인페이지

* 알림 목록
![image](https://github.com/user-attachments/assets/3908e749-a8c2-491b-bf1b-be36938c1e4f)

- 회원이 좋아요를 누른 직후 sse로 연결되어 서버에서 알림이 전송됨.
- 로그인 한 게시물 작성자에게 sse를 통해 알림내역 전송됨.
- 종모양 클릭 시 읽음처리.
- 알림내역에 안읽은 알림이 있으면 종모양에 빨간점 표시됨.

* 메인페이지

- 주간/월간 리스트 및 평점 높은 회사 순위
![image](https://github.com/user-attachments/assets/965e505b-d90e-4b3f-b413-1ee7dead5408)

- 최근 7일간 게시글 좋아요 수 베스트 10개 리스트
- 게시물 클릭 시 페이지 이동

- 투표 베스트 3
![image](https://github.com/user-attachments/assets/a9be58fd-7dd5-425c-ba17-fe83acb00d35)

- 최근 30일 게시글 좋아요 수 베스트 10개 리스트
- 게시물 클릭 시 페이지 이동


#### [BO] 리뷰 관리
![image](https://github.com/user-attachments/assets/0c2541bd-1104-4c4d-87e4-ab8bbb3ca27d)


- 리뷰 조회 가능
- (삭제 기능 추가 예정)

#### [BO] 회원 관리

* 회원 조회
![image](https://github.com/user-attachments/assets/e2f74391-df9b-4dfc-a773-713ea54a937c)

- 회원의 모든 정보를 조회 페이지

* 회원 인증
![image](https://github.com/user-attachments/assets/456381a5-29e6-4bf6-bdd6-75d9b8c949e5)
![image](https://github.com/user-attachments/assets/9dab9228-3ad0-4dcd-8f5c-7489da472132)

- 회원가입 시 관리자가 회원의 회사를 검증하는 페이지
- 회원 선택 후 회사 리스트에서 회사 선택 -> 저장 -> 검증완료

#### [BO] 게시판 관리

![image](https://github.com/user-attachments/assets/94b04902-1368-459e-816a-9469601957f0)

- 게시글 전체 목록 조회 페이지


#### [BO] 회사 관리

* 회사 조회
![image](https://github.com/user-attachments/assets/f64868f3-25f3-4cb4-bb0d-7c91e65228e7)

- 회사 모든 정보 조회 페이지

* 회사 인증
![image](https://github.com/user-attachments/assets/964f81c3-f5bf-48a2-b73e-da9146a7e7f5)

- 회원이 신청한 회사 인증하는 페이지
- 회사 선택 후 각각 회사 정보 확인한 뒤 저장 -> 회사 검증 완료


* * *



## 🎓 후기

#### __🛠️ 개선사항__
- 관리자 리뷰관리에 삭제 기능 추가하기
- 국세청 사업자등록 조회 API 또는 Open DART API 등 회사정보를 가져올 수 있는 api활용하기
- 날짜입력폼 달력선택으로 변경
- 메인화면에 통합검색기능 추가



#### __💡 후기__
- 프로젝트 기간동안 실제 프로젝트와 같이 진행해보며 다양한 산출물을 작성하고, 새로운 프론트엔드(vue3)를 활용하며 1인 프로젝트를 완성한 결과 많은 공부가 되었다.
- 회원들간 채팅 기능, 신고기능 등 기획단계에서 더 많은 아이디어가 나왔었는데 여건 상 구현하지 못한 부분들이 아쉬움으로 남는다.
- Github를 활용하여 원활한 팀 프로젝트를 진행할 수 있었다.

