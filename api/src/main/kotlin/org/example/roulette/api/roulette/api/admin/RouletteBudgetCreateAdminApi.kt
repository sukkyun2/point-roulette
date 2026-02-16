package org.example.roulette.api.roulette.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.roulette.app.RouletteBudgetCreateRequest
import org.example.roulette.api.roulette.app.RouletteBudgetCreateService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RouletteBudgetCreateAdminApi(
    private val rouletteBudgetCreateService: RouletteBudgetCreateService,
) {
    @PostMapping("/api/admin/budgets")
    fun createRouletteBudget(
        @RequestBody request: RouletteBudgetCreateRequest,
    ): ApiResponse<Unit> {
        rouletteBudgetCreateService.createRouletteBudget(request)
        return ApiResponse.ok()
    }
}
