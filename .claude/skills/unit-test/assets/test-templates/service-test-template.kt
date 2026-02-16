package com.example.service

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.*

class [SERVICE_NAME]Test : FunSpec({
    // 의존성 목 객체들
    val [dependency1Repository] = mockk<[Dependency1Repository]>()
    val [dependency2Service] = mockk<[Dependency2Service]>()
    lateinit var [serviceName]: [SERVICE_NAME]

    beforeTest {
        // 각 테스트 전에 목 객체 초기화
        clearMocks([dependency1Repository], [dependency2Service])
        [serviceName] = [SERVICE_NAME](
            [dependency1Repository] = [dependency1Repository],
            [dependency2Service] = [dependency2Service],
        )
    }

    context("[주요 기능] 관련 테스트") {
        test("[정상 케이스] - [구체적인 시나리오]") {
            // given
            val request = create[RequestType]()
            val expected = create[ResponseType]()
            
            every { [dependency1Repository].save(any()) } returns create[Entity]()
            justRun { [dependency2Service].notify(any()) }

            // when
            val result = [serviceName].[methodName](request)

            // then
            result shouldBe expected
            verify(exactly = 1) { [dependency1Repository].save(any()) }
            verify(exactly = 1) { [dependency2Service].notify(any()) }
        }

        test("[예외 케이스] - [구체적인 예외 상황]") {
            // given
            val invalidRequest = create[InvalidRequestType]()
            every { [dependency1Repository].findById(any()) } throws NoDataException()

            // when & then
            shouldThrow<[SpecificException]> {
                [serviceName].[methodName](invalidRequest)
            }
            
            verify(exactly = 1) { [dependency1Repository].findById(any()) }
            verify(exactly = 0) { [dependency2Service].notify(any()) }
        }

        test("[빈 데이터 케이스] - [빈 상태 처리]") {
            // given
            val emptyRequest = create[EmptyRequestType]()
            every { [dependency1Repository].findAll() } returns emptyList()

            // when
            val result = [serviceName].[methodName](emptyRequest)

            // then
            result shouldBe emptyList<[ResponseType]>()
            verify(exactly = 1) { [dependency1Repository].findAll() }
            verify(exactly = 0) { [dependency2Service].process(any()) }
        }
    }

    context("[보조 기능] 관련 테스트") {
        test("[검증 로직] - [특정 조건 검증]") {
            // given
            val validData = create[ValidDataType]()
            val slot = slot<[CaptureType]>()
            
            every { [dependency1Repository].save(capture(slot)) } answers { firstArg() }

            // when
            [serviceName].[helperMethod](validData)

            // then
            val capturedData = slot.captured
            capturedData.[property] shouldBe validData.[property]
            verify(exactly = 1) { [dependency1Repository].save(any()) }
        }
    }
}) {
    companion object {
        // 팩토리 메서드들
        private fun create[RequestType]([parameters]): [RequestType] {
            return [RequestType](
                // 기본 필드 값들
            )
        }

        private fun create[ResponseType]([parameters]): [ResponseType] {
            return [ResponseType](
                // 기본 필드 값들
            )
        }

        private fun create[Entity]([parameters]): [Entity] {
            return [Entity](
                // 기본 필드 값들
            )
        }
    }
}