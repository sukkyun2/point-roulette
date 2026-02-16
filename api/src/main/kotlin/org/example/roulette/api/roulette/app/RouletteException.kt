package org.example.roulette.api.roulette.app

open class RouletteParticipateFailureException(
    message: String?,
) : RuntimeException(message)

class AlreadyParticipatedTodayException : RouletteParticipateFailureException("오늘은 이미 룰렛에 참여했습니다")

class InsufficientBudgetException(
    message: String? = "오늘의 룰렛 예산이 부족합니다",
) : RouletteParticipateFailureException(message)

class EventPeriodException : RouletteParticipateFailureException("이벤트 기간이 아닙니다")
