package org.example.roulette.api.roulette.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.roulette.app.RouletteBudgetCreateRequest
import org.example.roulette.api.roulette.app.RouletteBudgetCreateService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/budget")
class RouletteBudgetCreateAdminApi(
    private val rouletteBudgetCreateService: RouletteBudgetCreateService,
) {
    @PostMapping
    fun createRouletteBudget(
        @RequestBody request: RouletteBudgetCreateRequest,
    ): ApiResponse<Nothing> {
        rouletteBudgetCreateService.createRouletteBudget(request)
        return ApiResponse.ok()
    }
}
