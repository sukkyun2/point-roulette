# Spring Kotlin 테스트 패턴

## 기본 테스트 구조

### Service 레이어 테스트
```kotlin
class UserServiceTest : FunSpec({
    val userRepository = mockk<UserRepository>()
    val emailService = mockk<EmailService>()
    lateinit var userService: UserService

    beforeTest {
        clearMocks(userRepository, emailService)
        userService = UserService(
            userRepository = userRepository,
            emailService = emailService,
        )
    }

    test("사용자 생성 시 정상적으로 저장되어야 한다") {
        // given
        val userRequest = createUserRequest()
        val savedUser = createUser(id = 1L)
        
        every { userRepository.save(any()) } returns savedUser
        justRun { emailService.sendWelcomeEmail(any()) }

        // when
        val result = userService.createUser(userRequest)

        // then
        result.id shouldBe 1L
        verify(exactly = 1) { userRepository.save(any()) }
        verify(exactly = 1) { emailService.sendWelcomeEmail(any()) }
    }
})
```

### Domain 레이어 테스트
```kotlin
class UserTest : FunSpec({
    test("사용자 이메일 변경 시 정상적으로 업데이트되어야 한다") {
        // given
        val user = createUser(email = "old@example.com")
        val newEmail = "new@example.com"

        // when
        user.changeEmail(newEmail)

        // then
        user.email shouldBe newEmail
    }

    test("잘못된 이메일 형식으로 변경 시 예외가 발생해야 한다") {
        // given
        val user = createUser()
        val invalidEmail = "invalid-email"

        // when & then
        shouldThrow<InvalidEmailException> {
            user.changeEmail(invalidEmail)
        }
    }
})
```

## 테스트 시나리오 패턴

### 1. 정상 처리 케이스
```kotlin
test("정상적인 데이터로 처리 시 성공해야 한다") {
    // given
    val validInput = createValidInput()
    every { dependency.process(any()) } returns successResult()

    // when
    val result = service.processData(validInput)

    // then
    result.isSuccess shouldBe true
    verify(exactly = 1) { dependency.process(any()) }
}
```

### 2. 예외 상황 처리
```kotlin
test("의존성에서 예외 발생 시 적절히 처리해야 한다") {
    // given
    val input = createInput()
    every { dependency.process(any()) } throws ServiceException("Error message")

    // when & then
    shouldThrow<ServiceException> {
        service.processData(input)
    }
    
    verify(exactly = 1) { dependency.process(any()) }
}
```

### 3. 빈 데이터/null 처리
```kotlin
test("빈 목록 입력 시 빈 결과를 반환해야 한다") {
    // given
    val emptyList = emptyList<Data>()

    // when
    val result = service.processDataList(emptyList)

    // then
    result shouldBe emptyList<ProcessedData>()
    verify(exactly = 0) { dependency.process(any()) }
}

test("null 입력 시 예외가 발생해야 한다") {
    // when & then
    shouldThrow<IllegalArgumentException> {
        service.processData(null)
    }
}
```

## 복잡한 시나리오 패턴

### 필터링 로직 테스트
```kotlin
test("조건에 맞는 항목만 필터링되어야 한다") {
    // given
    val mixedData = createMixedDataList()
    val slot = slot<List<Data>>()
    
    every { repository.findAll() } returns mixedData
    every { processor.process(capture(slot)) } returns processedResult()

    // when
    service.processActiveDataOnly()

    // then
    val capturedData = slot.captured
    capturedData.all { it.isActive } shouldBe true
    capturedData.size shouldBe 2
}
```

### 배치 처리 테스트
```kotlin
test("대용량 데이터 배치 처리 시 청크 단위로 처리되어야 한다") {
    // given
    val largeDataSet = createLargeDataSet(1000)
    val processedChunks = mutableListOf<List<Data>>()
    
    every { repository.findAll() } returns largeDataSet
    every { processor.processBatch(capture(slot<List<Data>>())) } answers {
        processedChunks.add(firstArg())
        emptyList()
    }

    // when
    service.processBatch()

    // then
    processedChunks.size shouldBe 10 // 100개씩 10번 처리
    processedChunks.all { it.size <= 100 } shouldBe true
}
```