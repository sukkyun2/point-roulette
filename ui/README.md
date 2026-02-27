# Point Roulette UI

## 기술 스택

* React 19, TypeScript 5.9
* pnpm workspace
* Vite, TailwindCSS
* TanStack Query
* Orval

---

## 주요 기능

### 사용자 앱
* 룰렛 참여
* 포인트 잔액 및 히스토리 조회
* 상품 목록 및 주문

### 관리자 앱
* 대시보드 및 통계 조회
* 룰렛 예산 관리
* 상품 CRUD 관리
* 주문 내역 및 취소 처리

---

## 프로젝트 구조

```
ui/packages/
├── user-app/          # 사용자 웹 애플리케이션
│   ├── src/
│   │   ├── components/    # UI 컴포넌트
│   │   ├── pages/         # 페이지 컴포넌트
│   │   ├── hooks/         # 커스텀 훅
│   │   └── api/           # API 클라이언트
│   └── package.json
├── admin-app/         # 관리자 웹 애플리케이션
└── shared/            # API 타입 정의, 공통 유틸리티
```

---

## 실행 방법

```bash
pnpm install

# API 타입 생성
pnpm dev:openapi

# 개발 서버 실행
pnpm dev:user    # 사용자 앱 (localhost:3000)
pnpm dev:admin   # 관리자 앱 (localhost:3001)
```

### 빌드

```bash
pnpm build
```

### 코드 품질

```bash
# 린팅 검사
pnpm lint

# 코드 포맷팅
pnpm format
```

---

## 환경 변수

```bash
# .env
VITE_API_BASE_URL=https://point-roulette-api.onrender.com
```

---

## 배포 환경

* **사용자 앱**: https://point-roulette-user-five.vercel.app/
* **관리자 앱**: https://point-roulette-admin-rho.vercel.app/