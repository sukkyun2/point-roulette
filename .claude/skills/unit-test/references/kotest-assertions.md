# Kotest 어설션 가이드

## 기본 어설션

### 값 비교
```kotlin
// 같음/다름 비교
result shouldBe expected
result shouldNotBe unexpected

// null 검사
value shouldBe null
value shouldNotBe null

// 타입 검사
result shouldBeInstanceOf<String>()
result shouldNotBeInstanceOf<Int>()
```

### 숫자 비교
```kotlin
// 정확한 값
count shouldBe 5
price shouldBe 1000.0

// 범위 비교
age shouldBeGreaterThan 18
score shouldBeLessThan 100
temperature shouldBeGreaterThanOrEqual 0
percentage shouldBeLessThanOrEqual 100

// 근사값 비교 (부동소수점)
pi shouldBe (3.14159 plusOrMinus 0.001)
```

### 문자열 어설션
```kotlin
// 문자열 비교
name shouldBe "김철수"
message shouldNotBe ""

// 문자열 패턴
email shouldStartWith "user"
filename shouldEndWith ".txt"
content shouldContain "중요한 내용"
url shouldMatch Regex("""https://.*\.com""")

// 대소문자 무시
text shouldBeEqualIgnoringCase "HELLO WORLD"

// 길이
password.length shouldBeGreaterThan 8
```

## 컬렉션 어설션

### 리스트/배열
```kotlin
// 크기
userList shouldHaveSize 3
emptyList shouldBeEmpty()
dataList shouldNotBeEmpty()

// 요소 포함
userList shouldContain user1
userList shouldContainAll listOf(user1, user2)
userList shouldContainExactly listOf(user1, user2, user3)
userList shouldContainExactlyInAnyOrder listOf(user3, user1, user2)

// 조건 검사
userList shouldContainOnly { it.isActive }
userList shouldContainAtLeastOne { it.isAdmin }
userList shouldContainNone { it.isBlocked }

// 정렬 검사
sortedList shouldBeSorted()
reversedList shouldBeSortedDescending()
```

### Map
```kotlin
// 키/값 존재
userMap shouldContainKey "userId"
userMap shouldContainValue user1
userMap shouldContainExactly mapOf("user1" to user1, "user2" to user2)

// 빈 맵
emptyMap shouldBeEmpty()
dataMap shouldNotBeEmpty()
```

## 예외 어설션

### 예외 발생 검사
```kotlin
// 특정 예외 타입
shouldThrow<IllegalArgumentException> {
    service.processInvalidData(null)
}

// 예외 메시지 검증
val exception = shouldThrow<ValidationException> {
    validator.validate(invalidData)
}
exception.message shouldBe "데이터가 유효하지 않습니다"

// 예외가 발생하지 않아야 하는 경우
shouldNotThrow<Exception> {
    service.processValidData(validData)
}

// 특정 타입의 예외가 발생하지 않아야 하는 경우
shouldNotThrowAny {
    service.safeOperation()
}
```

### 예외 세부 검증
```kotlin
val exception = shouldThrow<CustomException> {
    service.failOperation()
}

exception.apply {
    message shouldBe "예상된 오류 메시지"
    errorCode shouldBe "E001"
    cause shouldBeInstanceOf<NullPointerException>()
}
```

## 조건부 어설션

### Boolean 검사
```kotlin
result shouldBe true
flag shouldBe false

// 더 명확한 표현
user.isActive shouldBe true
validation.isValid shouldBe true
```

### 조건 검사
```kotlin
// 객체 속성 검사
user.age shouldBeGreaterThan 18
user.email shouldNotBe null
user.roles shouldContain Role.USER

// 복합 조건
user.apply {
    name shouldBe "김철수"
    age shouldBeGreaterThan 18
    isActive shouldBe true
    roles shouldContainAll listOf(Role.USER, Role.MEMBER)
}
```

## 고급 어설션

### 소프트 어설션 (모든 검증 실행)
```kotlin
test("사용자 정보가 모두 올바른지 검증한다") {
    // 하나가 실패해도 다른 검증들도 계속 실행
    assertSoftly(user) {
        name shouldBe "김철수"
        age shouldBeGreaterThan 18
        email shouldContain "@"
        isActive shouldBe true
        roles shouldNotBeEmpty()
    }
}
```

### 커스텀 매처
```kotlin
// 커스텀 매처 정의
fun beValidEmail() = object : Matcher<String> {
    override fun test(value: String) = MatcherResult(
        value.contains("@") && value.contains("."),
        "Expected $value to be a valid email",
        "Expected $value not to be a valid email"
    )
}

// 사용
email should beValidEmail()
```

### 데이터 클래스 어설션
```kotlin
// 전체 객체 비교
actualUser shouldBe expectedUser

// 특정 필드만 비교
actualUser shouldBe expectedUser.copy(updatedAt = actualUser.updatedAt)

// 부분 검증
actualUser.apply {
    id shouldBe expectedUser.id
    name shouldBe expectedUser.name
    // updatedAt은 검증하지 않음
}
```

## 시간 관련 어설션

### 날짜/시간 검사
```kotlin
createdAt shouldBeBefore Instant.now()
updatedAt shouldBeAfter createdAt
dueDate shouldBeBetween startDate.and(endDate)

// 특정 날짜
date shouldBe LocalDate.of(2024, 1, 1)
dateTime shouldBeToday()
timestamp shouldBeRecent(Duration.ofMinutes(5))
```

## 성능 관련 어설션

### 실행 시간 검증
```kotlin
test("작업이 지정된 시간 내에 완료되어야 한다") {
    shouldCompleteWithin(Duration.ofSeconds(1)) {
        service.processLargeData(data)
    }
}
```

## 컬렉션 고급 검증

### 순서와 내용 모두 중요한 경우
```kotlin
actualList shouldContainInOrder listOf(first, second, third)
```

### 부분 집합 검증
```kotlin
actualSet shouldContainAll expectedSubset
actualList shouldStartWith listOf(first, second)
actualList shouldEndWith listOf(last)
```

### 조건부 컬렉션 검증
```kotlin
users.filter { it.isActive } shouldHaveSize 5
users shouldHaveAtLeastSize 1
users shouldHaveAtMostSize 100

// 모든 요소가 조건을 만족
users.shouldForAll { user ->
    user.age shouldBeGreaterThan 0
    user.email shouldNotBe null
}

// 하나 이상의 요소가 조건을 만족
users.shouldForAny { user ->
    user.isAdmin shouldBe true
}
```