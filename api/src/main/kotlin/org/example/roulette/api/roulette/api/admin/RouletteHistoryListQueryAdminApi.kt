package org.example.roulette.api.roulette.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.roulette.app.RouletteHistoryListQueryService
import org.example.roulette.api.roulette.app.RouletteParticipationHistoryListQueryResponse
import org.example.roulette.api.roulette.domain.RouletteStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class RouletteHistoryListQueryAdminApi(
    private val rouletteHistoryListQueryService: RouletteHistoryListQueryService,
) {
    @GetMapping("/api/admin/roulettes/participations")
    fun getParticipationHistories(
        @RequestParam(required = false) status: RouletteStatus?,
    ): ApiResponse<RouletteParticipationHistoryListQueryResponse> {
        val response = rouletteHistoryListQueryService.getParticipationHistories(status)
        return ApiResponse.ok(response)
    }
}
