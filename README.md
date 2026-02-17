# 구현 스펙

## 디렉토리 구조

```sql
  point-roulette/
  ├── api/                    # Spring Boot Kotlin API 서버
  ├── mobile/                 # Flutter 모바일 앱
  ├── ui/                     # React 웹 UI
    ├── packages
        ├── user-app          # 사용자 페이지
        ├── shared            # 공유하는 내용(API Schema, package 등)
        ├── admid-app         # 관리자 페이지
  ├── prompt/                 # 사용한 프롬프트 
```

## 웹 어드민 구현 사항

- [x]  **대시보드**
- [x]  **예산 관리**
    - [x]  일일 예산 설정/조회
    - [x]  **룰렛 참여 취소(포인트 회수)** 기능
- [x]  상품 관리
    - [x]  상품 CRUD
    - [ ]  재고 관리
- [x]  **주문 내역**
    - [x]  주문 목록
    - [x]  **주문 취소(포인트 환불)** 기능

## 사용자 구현 사항

- [x]  **로그인**
    - [x]  닉네임 입력 (로그인 Mocking)
- [x]  **홈 (룰렛)**
    - [x]  룰렛 UI
    - [x]  오늘 잔여 예산 표시
    - [x]  룰렛 참여
- [x]  **내 포인트**
    - [x]  포인트 목록 (유효기간, **만료됨** 상태 표시)
    - [x]  **7일 내 만료 예정 포인트** 알림
    - [x]  전체 사용 가능한 포인트 합
- [x]  **상품 목록**
    - [x]  구매 가능 상품
    - [x]  내 포인트로 구매 가능 여부
- [x]  **주문 내역**
    - [x]  내 주문 목록

## 플러터

- [x]  웹 프론트엔드를 WebView로 렌더링
- [x]  Android 동작, 뒤로가기 처리
- [x]  로그인 상태 유지
- [x]  **앱 아이콘 & 이름 변경**: 기본 Flutter 아이콘/이름 대신, 서비스에 맞는 것으로 변경
- [x]  **네트워크 에러 처리**: 인터넷 연결 끊김이나 페이지 로딩 실패 시 **커스텀 에러 페이지** 표시 및 ‘재시도’ 버튼 구현
- [ ]  **로딩 처리**: WebView 로딩 중 **네이티브 인디케이터(Spinner)** 표시
- [x]  **스플래시 스크린(Splash Screen)**: 앱 실행 시 네이티브 스플래시 화면 적용

## 기술 스택

```sql
* 사용자 웹 페이지
  - React: 19.2.0
  - React DOM: 19.2.0
  - TypeScript: 5.9.3
  - Vite: 7.3.1
  - TailwindCSS: 3.4.0
  - TanStack React Query: 5.90.21
  - react-custom-roulette: 1.4.1

* 어드민 웹 페이지
  - React: 19.2.0
  - React DOM: 19.2.0
  - TypeScript: 5.9.3
  - Vite: 7.3.1
  - TailwindCSS: 3.4.0

* 웹 페이지 공통 의존성
  - Axios: 1.13.5
  - Orval: 7.21.0 (API 코드 생성)
  - ESLint: 9.39.1
  - Prettier: 3.8.1
  
* 백엔드
  - Kotlin: 2.2.21
  - Spring Boot: 4.0.2
  - PostgreSQL (운영)
  - H2 (테스트)
  - Redis (Spring Data Redis)
  - Redisson: 4.2.0
  - SpringDoc OpenAPI: 2.7.0
  - Kotest: 5.8.0
  - MockK: 1.13.8
  - Gradle: 8.x
  - ktlint: 1.7.0
  
* 플러터 앱
  - Flutter 3.12
  - webview_flutter 4.4.2
  - flutter_dotenv 5.1.0
 
```

## 배포 플랫폼

- **프론트엔드/어드민**: [Vercel](https://vercel.com/)
- **백엔드**: [Render](https://render.com/)
- **데이터베이스**: [Neon](https://neon.tech/)
- CI/CD 파이프라인 : [Github Actions Workflow](https://github.com/sukkyun2/point-roulette/blob/main/.github/workflows/deploy-render.yml)

## 테스트 시 유의사항

### Render 관련

- Render로 배포를 진행하다보니 일정시간이 지나면 서버에 응답이 없는 문제가 있습니다.
- 테스트하실 때, Render API를 먼저 실행하고 Service Up이 됐을 때 테스트 부탁드립니다.

### 로그인 관련

- 사용자는 A부터 Z까지 26개의 유저를 미리 생성하였습니다.
- 로그인 화면에서 대문자 하나 입력시 로그인 가능합니다.
- 어드민은 별도 로그인이 필요없습니다.

### 프롬프트 관련
- 프롬프트를 [prompt.md](https://github.com/sukkyun2/point-roulette/blob/main/prompt/prompt.md) 파일에 담긴했지만 별도로 저장한 MD 파일 첨부합니다.

## 접속 주소

- 프론트엔드 접속 URL : [https://point-roulette-user-five.vercel.app/](https://point-roulette-user-five.vercel.app/)
- 어드민 접속 URL : [https://point-roulette-admin-56bw99imv-goobghd-gmailcoms-projects.vercel.app/](https://point-roulette-admin-56bw99imv-goobghd-gmailcoms-projects.vercel.app/)
- 백엔드 API 문서 : [https://point-roulette-api.onrender.com/swagger-ui/index.html](https://point-roulette-api.onrender.com/swagger-ui/index.html)
- Flutter 앱 APK 파일 : https://drive.google.com/file/d/1jbvRVlpHJ2nZiXXLvrcK0zw64EbnV7Yx/view?usp=drive_link