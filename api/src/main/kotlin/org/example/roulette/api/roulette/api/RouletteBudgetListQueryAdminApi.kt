package org.example.roulette.api.roulette.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.roulette.app.RouletteBudgetListQueryResponse
import org.example.roulette.api.roulette.app.RouletteBudgetListQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/budget")
class RouletteBudgetListQueryAdminApi(
    private val rouletteBudgetListQueryService: RouletteBudgetListQueryService,
) {
    @GetMapping
    fun getRouletteBudgetList(): ApiResponse<RouletteBudgetListQueryResponse> {
        val response = rouletteBudgetListQueryService.getRouletteBudgetList()
        return ApiResponse.success(response)
    }
}
