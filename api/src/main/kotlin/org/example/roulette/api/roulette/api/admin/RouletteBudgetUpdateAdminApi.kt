package org.example.roulette.api.roulette.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.roulette.app.RouletteBudgetUpdateRequest
import org.example.roulette.api.roulette.app.RouletteBudgetUpdateService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RouletteBudgetUpdateAdminApi(
    private val rouletteBudgetUpdateService: RouletteBudgetUpdateService,
) {
    @PutMapping("/api/admin/budgets/{budgetId}")
    fun updateRouletteBudget(
        @PathVariable budgetId: Long,
        @RequestBody request: RouletteBudgetUpdateRequest
    ): ApiResponse<Nothing> {
        rouletteBudgetUpdateService.updateRouletteBudget(request)
        return ApiResponse.ok()
    }
}
