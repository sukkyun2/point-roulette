package org.example.roulette.api.roulette.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.roulette.app.RouletteBudgetUpdateRequest
import org.example.roulette.api.roulette.app.RouletteBudgetUpdateService
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/budget")
class RouletteBudgetUpdateAdminApi(
    private val rouletteBudgetUpdateService: RouletteBudgetUpdateService,
) {
    @PutMapping
    fun updateRouletteBudget(
        @RequestBody request: RouletteBudgetUpdateRequest,
    ): ApiResponse<Nothing> {
        rouletteBudgetUpdateService.updateRouletteBudget(request)
        return ApiResponse.ok()
    }
}
