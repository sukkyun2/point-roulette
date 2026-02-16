---
name: unit-test
description: Spring Boot Kotlin 단위 테스트 작성 지원. Kotest와 MockK를 사용한 Service 및 Domain 레이어 테스트 코드 작성 시 사용. 테스트 메서드는 한글로 작성하며, MockK를 활용한 목킹, Kotest 어설션 활용, 팩토리 메서드 패턴 적용. 사용자가 "테스트 코드 작성", "단위 테스트", "MockK", "Kotest" 관련 요청 시 활성화.
---

# Unit Test

Spring Boot + Kotlin + Kotest + MockK 스택을 사용한 효과적인 단위 테스트 작성을 지원합니다.

## 핵심 기능

### 1. Service 레이어 테스트
Service 클래스의 비즈니스 로직 테스트를 위한 패턴과 템플릿을 제공합니다.

```kotlin
class UserServiceTest : FunSpec({
    val userRepository = mockk<UserRepository>()
    val emailService = mockk<EmailService>()
    lateinit var userService: UserService

    beforeTest {
        clearMocks(userRepository, emailService)
        userService = UserService(userRepository, emailService)
    }

    test("사용자 생성 시 정상적으로 저장되어야 한다") {
        // given
        val request = createUserRequest()
        every { userRepository.save(any()) } returns createUser(id = 1L)
        
        // when
        val result = userService.createUser(request)
        
        // then
        result.id shouldBe 1L
        verify(exactly = 1) { userRepository.save(any()) }
    }
})
```

### 2. Domain 레이어 테스트
도메인 객체의 비즈니스 규칙과 상태 변경 로직 테스트를 지원합니다.

```kotlin
class UserTest : FunSpec({
    test("이메일 변경 시 정상적으로 업데이트되어야 한다") {
        // given
        val user = createUser(email = "old@example.com")
        
        // when
        user.changeEmail("new@example.com")
        
        // then
        user.email shouldBe "new@example.com"
    }
})
```

### 3. 테스트 패턴 가이드
정상 처리, 예외 상황, 빈 데이터 처리의 3가지 핵심 테스트 패턴을 제공합니다.

**상세한 테스트 패턴**: [test-patterns.md](references/test-patterns.md)를 참조하세요.

### 4. MockK 활용 가이드
Repository와 Service 목킹을 위한 상세한 가이드를 제공합니다.

**MockK 사용법**: [mocking-guide.md](references/mocking-guide.md)를 참조하세요.

### 5. Kotest 어설션 가이드
다양한 데이터 타입과 컬렉션에 대한 어설션 패턴을 제공합니다.

**Kotest 어설션**: [kotest-assertions.md](references/kotest-assertions.md)를 참조하세요.

## 템플릿 사용 방법

테스트 작성 시 다음 템플릿들을 활용할 수 있습니다:

### Service 테스트 템플릿
[service-test-template.kt](assets/test-templates/service-test-template.kt)에서 Service 레이어 테스트의 기본 구조를 확인할 수 있습니다.

### Domain 테스트 템플릿  
[domain-test-template.kt](assets/test-templates/domain-test-template.kt)에서 Domain 객체 테스트의 기본 구조를 확인할 수 있습니다.

## 작성 가이드라인

1. **테스트 메서드명**: 한글로 작성하고 구체적인 시나리오를 명시
2. **팩토리 메서드**: companion object에 create[ObjectName] 형태로 작성
3. **Given-When-Then**: 명확한 단계별 주석으로 구분
4. **목 초기화**: beforeTest에서 clearMocks로 상태 초기화
5. **검증**: 정확한 호출 횟수와 파라미터 검증
