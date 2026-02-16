package org.example.roulette.api.roulette.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.common.api.SwaggerApiResponse
import org.example.roulette.api.roulette.app.AdminDashboardQueryResponse
import org.example.roulette.api.roulette.app.AdminDashboardQueryService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/admin/roulette")
class AdminDashboardQueryApi(
    private val adminDashboardQueryService: AdminDashboardQueryService,
) {
    @GetMapping("/dashboard")
    fun getDashboard(
        @RequestParam(required = false)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        date: LocalDate?,
    ): ApiResponse<AdminDashboardQueryResponse> {
        val targetDate = date ?: LocalDate.now()
        val response = adminDashboardQueryService.getDashboard(targetDate)
        return ApiResponse.ok(response)
    }
}
