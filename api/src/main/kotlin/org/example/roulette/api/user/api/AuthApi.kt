package org.example.roulette.api.user.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.user.app.AuthService
import org.example.roulette.api.user.app.LoginResult
import org.example.roulette.config.auth.CurrentUser
import org.example.roulette.config.auth.SimpleUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthApi(
    private val authService: AuthService,
) {
    @PostMapping("/api/auth/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ApiResponse<LoginResult> {
        val result = authService.login(request.nickname)
        return ApiResponse.ok(result)
    }

    @GetMapping("/api/auth/me")
    fun getCurrentUser(
        @CurrentUser user: SimpleUser,
    ): ApiResponse<SimpleUser> = ApiResponse.ok(user)
}

data class LoginRequest(
    val nickname: String,
)
