# Point Roulette API

## 기술 스택

* Kotlin, Spring Boot
* PostgreSQL, Redis
* Spring Data JPA
* Kotest, MockK

---

## 주요 기능

### 룰렛

* Redis 분산 락을 통한 동시성 제어
* 일일 지급 예산 관리
* 포인트 지급 정책 적용

### 포인트

* 포인트 적립 / 차감
* 잔액 조회
* 적립/사용 이력 관리
* 만료 기간 관리

### 상품 및 주문

* 상품 CRUD
* 포인트 사용 주문
* 주문 취소 시 포인트 환불

---

## 프로젝트 구조

```
api/src/main/kotlin/org/example/roulette/
├── api/
│   ├── common      # 공통 ApiReponse, ExceptionHandler 등
│   ├── user
│   ├── roulette
│   ├── point
│   ├── product
│   └── order
└── config          # Redis, Authentication, Swagger 설정 등
```

---

## 실행 방법

```bash
./gradlew bootRun
```

### 테스트

```bash
./gradlew test
```

### ktlint
```bash
./gradlew ktlintFormat
```

---

## 환경 변수

```bash
export DB_URL=jdbc:postgresql://localhost:5432/point_roulette
export DB_USERNAME=user
export DB_PASSWORD=password
export REDIS_HOST=localhost
export REDIS_PORT=6379
export JWT_SECRET=your-secret-key
```



---

## API 문서

* http://localhost:8080/swagger-ui/index.html
* https://point-roulette-api.onrender.com/swagger-ui/index.html

---

## 데이터베이스

* PostgreSQL
* Flyway 기반 스키마 관리

    * 위치: `src/main/resources/db/migration`

---

## 배포 환경

* Deploy: Render
* Database: Neon
* CI/CD: GitHub Actions