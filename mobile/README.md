# Point Roulette Mobile

## 기술 스택

* Flutter 3.12, Dart 3.12
* webview_flutter
* Flutter Dotenv
* Gradle

---

## 주요 기능

### WebView 통합
* React 웹 앱을 네이티브 WebView로 렌더링
* 웹 스토리지 상태 유지

### 네이티브 기능
* Android 물리 버튼 지원
* 커스텀 스플래시 스크린
* 네이티브 앱 아이콘 및 로딩 인디케이터

### 오프라인 처리
* 네트워크 연결 상태 모니터링
* 연결 실패 시 재시도 메커니즘
* 커스텀 에러 페이지 제공

---

## 프로젝트 구조

```
mobile/
├── android/           # Android 네이티브 설정
├── lib/              # Dart 소스 코드 (main.dart, config)
└── assets/           # 정적 자원 (fallback.html)
```

---

## 실행 방법

```bash
# 의존성 설치
flutter pub get

# 환경 파일 설정
echo "WEB_URL=https://point-roulette-user-five.vercel.app/" > .env

# 개발 실행
flutter run
```

### 빌드

```bash
flutter build apk --release
```

### 테스트

```bash
flutter test
```

---

## 환경 변수

```bash
# .env
WEB_URL=https://point-roulette-user-five.vercel.app/
```

---

## 배포 환경

* **APK 다운로드**: [Google Drive](https://drive.google.com/file/d/1jbvRVlpHJ2nZiXXLvrcK0zw64EbnV7Yx/view?usp=drive_link)
* **웹 앱 연결**: https://point-roulette-user-five.vercel.app/