package org.example.roulette.api.roulette.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.roulette.app.RouletteBudgetListQueryResponse
import org.example.roulette.api.roulette.app.RouletteBudgetListQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RouletteBudgetListQueryAdminApi(
    private val rouletteBudgetListQueryService: RouletteBudgetListQueryService,
) {
    @GetMapping("/api/admin/budgets")
    fun getRouletteBudgetList(): ApiResponse<RouletteBudgetListQueryResponse> {
        val response = rouletteBudgetListQueryService.getRouletteBudgetList()
        return ApiResponse.ok(response)
    }
}
