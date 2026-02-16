package org.example.roulette.api.roulette.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.common.app.ValidationException
import org.example.roulette.api.roulette.app.InsufficientBudgetException
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
        @RequestBody request: RouletteBudgetUpdateRequest,
    ): ApiResponse<Unit> {
        try {
            rouletteBudgetUpdateService.updateRouletteBudget(request)
            return ApiResponse.ok()
        } catch (ex: InsufficientBudgetException) {
            return ApiResponse.badRequest(ex.message)
        } catch (ex: ValidationException) {
            return ApiResponse.badRequest(ex.message)
        }
    }
}
