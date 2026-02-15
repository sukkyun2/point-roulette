package org.example.roulette.api.point.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.point.app.PointHistoryQueryResponse
import org.example.roulette.api.point.app.PointHistoryQueryService
import org.example.roulette.api.point.domain.PointType
import org.example.roulette.config.auth.CurrentUser
import org.example.roulette.config.auth.SimpleUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class PointHistoryQueryApi(
    private val pointHistoryQueryService: PointHistoryQueryService,
) {
    @GetMapping("/api/point/history")
    fun getPointHistory(
        @CurrentUser user: SimpleUser,
    ): ApiResponse<PointHistoryQueryResponse> = ApiResponse.ok(pointHistoryQueryService.getHistories(user.id))
}
