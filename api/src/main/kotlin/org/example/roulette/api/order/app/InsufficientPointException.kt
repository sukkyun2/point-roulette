package org.example.roulette.api.order.app

class InsufficientPointException(
    message: String? = "잔액이 부족합니다",
) : RuntimeException(message)
