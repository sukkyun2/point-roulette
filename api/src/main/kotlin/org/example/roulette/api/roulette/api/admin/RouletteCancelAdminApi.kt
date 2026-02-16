package org.example.roulette.api.roulette.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.roulette.app.RouletteCancelService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RouletteCancelAdminApi(
    private val rouletteCancelService: RouletteCancelService,
) {
    @PostMapping("/api/admin/roulettes/{rouletteHistoryId}/cancel")
    fun cancelRouletteParticipation(
        @PathVariable rouletteHistoryId: Long,
    ): ApiResponse<Nothing> =
        try {
            rouletteCancelService.cancelParticipation(rouletteHistoryId)
            ApiResponse.ok()
        } catch (ex: IllegalStateException) {
            ApiResponse.badRequest(ex.message)
        } catch (ex: IllegalArgumentException) {
            ApiResponse.badRequest(ex.message)
        }
}
