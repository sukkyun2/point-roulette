package org.example.roulette.api.roulette.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.roulette.app.AdminDashboardQueryResponse
import org.example.roulette.api.roulette.app.AdminDashboardQueryService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class DashboardQueryAdminApi(
    private val adminDashboardQueryService: AdminDashboardQueryService,
) {
    @GetMapping("/api/admin/roulettes/statistics")
    fun getDashboard(
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        date: LocalDate?,
    ): ApiResponse<AdminDashboardQueryResponse> {
        val targetDate = date ?: LocalDate.now()
        val response = adminDashboardQueryService.getDashboard(targetDate)
        return ApiResponse.Companion.ok(response)
    }
}
