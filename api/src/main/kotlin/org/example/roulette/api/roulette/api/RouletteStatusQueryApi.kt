package org.example.roulette.api.roulette.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.common.api.SwaggerApiResponse
import org.example.roulette.api.roulette.app.RouletteStatusQueryService
import org.example.roulette.config.auth.CurrentUser
import org.example.roulette.config.auth.SimpleUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RouletteStatusQueryApi(
    private val rouletteStatusQueryService: RouletteStatusQueryService,
) {
    @GetMapping("/api/roulette/status")
    @SwaggerApiResponse(schema = RouletteStatusQueryResponse::class)
    fun getStatus(
        @CurrentUser user: SimpleUser,
    ): ApiResponse<RouletteStatusQueryResponse> {
        val status = rouletteStatusQueryService.getStatus(user.id)
        return ApiResponse.ok(status)
    }
}