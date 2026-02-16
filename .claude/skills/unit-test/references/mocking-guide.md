# MockK 사용 가이드

## 기본 목킹 패턴

### Mock 객체 생성 및 초기화
```kotlin
class ServiceTest : FunSpec({
    val repository = mockk<Repository>()
    val externalService = mockk<ExternalService>()
    lateinit var service: Service

    beforeTest {
        clearMocks(repository, externalService)
        service = Service(repository, externalService)
    }
})
```

### Repository 목킹 패턴
```kotlin
// 단일 객체 반환
every { userRepository.findById(1L) } returns createUser(id = 1L)

// Optional 반환
every { userRepository.findById(1L) } returns Optional.of(createUser(id = 1L))
every { userRepository.findById(999L) } returns Optional.empty()

// 리스트 반환
every { userRepository.findAll() } returns listOf(createUser(1L), createUser(2L))
every { userRepository.findByStatus(UserStatus.ACTIVE) } returns emptyList()

// 저장 동작
every { userRepository.save(any<User>()) } answers { firstArg() }
every { userRepository.saveAll(any<List<User>>()) } returns listOf()

// 삭제 동작 (void 메서드)
justRun { userRepository.delete(any()) }
justRun { userRepository.deleteById(any()) }

// 카운트 반환
every { userRepository.count() } returns 10L
every { userRepository.existsById(1L) } returns true
every { userRepository.existsById(999L) } returns false
```

### Service 목킹 패턴
```kotlin
// 정상 응답
every { emailService.sendEmail(any(), any()) } returns EmailResult.success()

// void 메서드
justRun { emailService.sendWelcomeEmail(any()) }

// 예외 발생
every { paymentService.processPayment(any()) } throws PaymentException("결제 실패")

// 조건부 응답
every { authService.validateToken(any()) } answers {
    val token = firstArg<String>()
    if (token == "valid-token") true else false
}
```

## 고급 목킹 패턴

### Slot을 이용한 파라미터 캡처
```kotlin
test("전달된 파라미터를 검증해야 한다") {
    // given
    val userSlot = slot<User>()
    val emailSlot = slot<String>()
    
    every { userRepository.save(capture(userSlot)) } answers { firstArg() }
    justRun { emailService.sendEmail(capture(emailSlot), any()) }

    // when
    service.createUser(createUserRequest(email = "test@example.com"))

    // then
    userSlot.captured.email shouldBe "test@example.com"
    emailSlot.captured shouldBe "test@example.com"
}
```

### 여러 호출에 대한 순차적 응답
```kotlin
every { externalApi.getData() } returnsMany listOf(
    ApiResponse("첫 번째 응답"),
    ApiResponse("두 번째 응답"),
    ApiResponse("세 번째 응답")
)
```

### 조건부 목킹
```kotlin
every { 
    userRepository.findByEmail(match { it.endsWith("@admin.com") }) 
} returns createAdminUser()

every { 
    userRepository.findByEmail(match { !it.endsWith("@admin.com") }) 
} returns createRegularUser()
```

### 복잡한 매개변수 매칭
```kotlin
// 특정 조건의 객체만 매칭
every { 
    orderService.processOrder(match { order -> 
        order.amount > 0 && order.status == OrderStatus.PENDING 
    })
} returns ProcessResult.success()

// 리스트 내용 검증
every { 
    batchProcessor.process(match<List<Data>> { list -> 
        list.isNotEmpty() && list.all { it.isValid } 
    })
} returns BatchResult.success()
```

## Verify 패턴

### 기본 검증
```kotlin
// 정확히 한 번 호출
verify(exactly = 1) { repository.save(any()) }

// 호출되지 않음
verify(exactly = 0) { emailService.sendEmail(any(), any()) }

// 최소/최대 호출 횟수
verify(atLeast = 1) { logger.info(any()) }
verify(atMost = 3) { retryService.attempt(any()) }

// 호출 순서 검증
verifyOrder {
    repository.save(any())
    emailService.sendEmail(any(), any())
}
```

### 특정 파라미터로 호출 검증
```kotlin
verify { userRepository.findById(1L) }
verify { emailService.sendEmail("test@example.com", any()) }
verify { 
    orderService.processOrder(match { it.id == 1L }) 
}
```

### 모든 목 검증
```kotlin
confirmVerified(repository, emailService, externalApi)
```

## 일반적인 실수 방지

### clearMocks 사용
```kotlin
beforeTest {
    // 각 테스트 전에 목 상태 초기화
    clearMocks(repository, service1, service2)
}
```

### relaxed mock 사용 시 주의
```kotlin
// relaxed mock은 기본값을 반환하지만 명시적 설정이 좋음
val repository = mockk<Repository>(relaxed = true)

// 명시적으로 설정하는 것이 좋음
every { repository.findById(any()) } returns null
```

### 예외 상황 목킹
```kotlin
// 특정 조건에서만 예외 발생
every { 
    paymentService.charge(match { amount -> amount <= 0 }) 
} throws IllegalArgumentException("금액은 0보다 커야 합니다")

every { 
    paymentService.charge(match { amount -> amount > 0 }) 
} returns ChargeResult.success()
```