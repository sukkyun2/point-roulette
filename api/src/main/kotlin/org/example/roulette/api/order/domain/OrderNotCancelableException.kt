package org.example.roulette.api.order.domain

class OrderNotCancelableException(
    message: String? = "주문 취소 상태는 취소할 수 없습니다",
) : RuntimeException(message)
