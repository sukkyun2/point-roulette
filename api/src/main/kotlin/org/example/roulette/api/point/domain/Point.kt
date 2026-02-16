package org.example.roulette.api.point.domain

data class Point(
    val value: Long,
) {
    init {
        require(value >= 0) { "포인트는 음수일 수 없습니다." }
    }
}
