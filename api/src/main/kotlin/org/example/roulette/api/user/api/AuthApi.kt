package org.example.roulette.api.user.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.user.app.AuthService
import org.example.roulette.config.auth.CurrentUser
import org.example.roulette.config.auth.SimpleUser
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/api/auth/login")
    fun login(@RequestBody request: LoginRequest): ApiResponse<out Any?> {
        try {
            val result = authService.login(request.nickname)
            return ApiResponse.ok(result)
        } catch (_: NoDataException){
            return ApiResponse.nodata()
        }
    }

    @GetMapping("/api/auth/me")
    fun getCurrentUser(@CurrentUser user: SimpleUser): ApiResponse<out Any?> {
        return ApiResponse.ok(user)
    }
}

data class LoginRequest(
    val nickname: String
)