package org.example.roulette.api.roulette.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.roulette.app.RouletteParticipateFailureException
import org.example.roulette.api.roulette.app.RouletteParticipateResponse
import org.example.roulette.api.roulette.app.RouletteParticipateService
import org.example.roulette.config.auth.CurrentUser
import org.example.roulette.config.auth.SimpleUser
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RouletteParticipateApi(
    private val rouletteParticipateService: RouletteParticipateService,
) {
    @PostMapping("/api/roulettes/current/participants")
    fun participate(
        @CurrentUser user: SimpleUser,
    ): ApiResponse<RouletteParticipateResponse> =
        try {
            val point = rouletteParticipateService.participate(user.id)
            ApiResponse.ok(RouletteParticipateResponse(point.value))
        } catch (ex: RouletteParticipateFailureException) {
            ApiResponse.badRequest(ex.message)
        }
}
