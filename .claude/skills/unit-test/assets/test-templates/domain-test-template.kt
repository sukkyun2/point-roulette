package com.example.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow

class [DOMAIN_CLASS]Test : FunSpec({
    
    context("[도메인 객체] 생성 테스트") {
        test("정상적인 값으로 생성 시 올바르게 초기화되어야 한다") {
            // given
            val [param1] = "[validValue1]"
            val [param2] = "[validValue2]"

            // when
            val [domainObject] = [DOMAIN_CLASS](
                [param1] = [param1],
                [param2] = [param2]
            )

            // then
            [domainObject].[param1] shouldBe [param1]
            [domainObject].[param2] shouldBe [param2]
            [domainObject].[derivedProperty] shouldBe [expectedValue]
        }

        test("잘못된 값으로 생성 시 예외가 발생해야 한다") {
            // given
            val invalid[Param1] = "[invalidValue]"

            // when & then
            shouldThrow<[SpecificException]> {
                [DOMAIN_CLASS](
                    [param1] = invalid[Param1],
                    [param2] = "[validValue]"
                )
            }
        }
    }

    context("[도메인 로직] 테스트") {
        test("[비즈니스 규칙] - [구체적인 규칙 설명]") {
            // given
            val [domainObject] = create[DomainClass]()
            val [inputParameter] = "[validInput]"

            // when
            [domainObject].[businessMethod]([inputParameter])

            // then
            [domainObject].[changedProperty] shouldBe [expectedValue]
            [domainObject].[statusProperty] shouldBe [ExpectedStatus].[VALUE]
        }

        test("[상태 변경] - [상태 전이 시나리오]") {
            // given
            val [domainObject] = create[DomainClass](status = [InitialStatus].[VALUE])

            // when
            [domainObject].[stateChangeMethod]()

            // then
            [domainObject].status shouldBe [ExpectedStatus].[VALUE]
            [domainObject].[relatedProperty] shouldNotBe null
        }

        test("[유효성 검증] - [잘못된 상태에서 작업 시 예외 발생]") {
            // given
            val [domainObject] = create[DomainClass](status = [InvalidStatus].[VALUE])

            // when & then
            shouldThrow<[DomainException]> {
                [domainObject].[restrictedMethod]()
            }
        }
    }

    context("[값 객체] 테스트") {
        test("[동등성 비교] - 같은 값의 객체는 동일해야 한다") {
            // given
            val [valueObject1] = [ValueObject]("[sameValue]")
            val [valueObject2] = [ValueObject]("[sameValue]")

            // when & then
            [valueObject1] shouldBe [valueObject2]
            [valueObject1].hashCode() shouldBe [valueObject2].hashCode()
        }

        test("[불변성] - 값 객체는 변경되지 않아야 한다") {
            // given
            val original[Value] = "[originalValue]"
            val [valueObject] = [ValueObject](original[Value])

            // when
            val new[ValueObject] = [valueObject].change[Property]("[newValue]")

            // then
            [valueObject].[property] shouldBe original[Value]  // 원본 불변
            new[ValueObject].[property] shouldBe "[newValue]"  // 새 객체 생성
        }
    }

    context("[도메인 규칙] 검증 테스트") {
        test("[복합 규칙] - [여러 조건을 만족해야 하는 시나리오]") {
            // given
            val [domainObject] = create[DomainClass]([specificCondition] = true)
            val [criteria] = [Criteria]([param1] = "[value1]", [param2] = "[value2]")

            // when
            val result = [domainObject].can[PerformAction]([criteria])

            // then
            result shouldBe true
        }

        test("[비즈니스 제약] - [제약 조건 위반 시 처리]") {
            // given
            val [domainObject] = create[DomainClass]()
            val violating[Criteria] = [Criteria]([restrictedValue] = "[invalidValue]")

            // when & then
            shouldThrow<[BusinessRuleException]> {
                [domainObject].[restrictedOperation](violating[Criteria])
            }
        }
    }
}) {
    companion object {
        // 팩토리 메서드들
        private fun create[DomainClass](
            [param1]: [Type1] = "[defaultValue1]",
            [param2]: [Type2] = "[defaultValue2]",
            status: [StatusEnum] = [StatusEnum].[DEFAULT]
        ): [DomainClass] {
            return [DomainClass](
                [param1] = [param1],
                [param2] = [param2],
                status = status
            )
        }

        private fun create[RelatedEntity](
            [param]: [Type] = "[defaultValue]"
        ): [RelatedEntity] {
            return [RelatedEntity](
                [param] = [param]
            )
        }
    }
}