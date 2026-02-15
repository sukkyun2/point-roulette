package org.example.roulette.api.user.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.common.api.SwaggerApiResponse
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
    @SwaggerApiResponse(schema = LoginResult::class)
    @PostMapping("/api/auth/login")
    fun login(
        @RequestBody request: LoginRequest,
    ): ApiResponse<LoginResult> {
        val result = authService.login(request.nickname)
        return ApiResponse.ok(result)
    }

    @SwaggerApiResponse(schema = SimpleUser::class)
    @GetMapping("/api/auth/me")
    fun getCurrentUser(
        @CurrentUser user: SimpleUser,
    ): ApiResponse<SimpleUser> = ApiResponse.ok(user)
}

@Schema(description = "로그인 요청 데이터")
data class LoginRequest(
    @Schema(description = "사용자 닉네임", example = "홍길동")
    val nickname: String,
)
