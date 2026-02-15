package org.example.roulette.api.common.api

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "ApiResponse", description = "API 응답 공통 포맷")
data class ApiResponse<out T>(
    @Schema(description = "응답 코드", example = "200")
    val code: String,
    @Schema(description = "응답 메시지", example = "성공")
    val message: String?,
    @Schema(description = "응답 데이터")
    val data: T?,
) {
    companion object {
        fun <T> ok(data: T): ApiResponse<T> = ApiResponse("200", "성공", data)

        fun ok(): ApiResponse<Nothing> = ApiResponse("200", "성공", null)

        fun badRequest(message: String?): ApiResponse<Nothing> = ApiResponse("400", message, null)

        fun badRequest(): ApiResponse<Nothing> = ApiResponse("400", null, null)

        fun unauthorized(): ApiResponse<Nothing> = ApiResponse("401", "unauthorized", null)

        fun error(message: String): ApiResponse<Nothing> = ApiResponse("500", message, null)

        fun nodata(): ApiResponse<Nothing> = ApiResponse("404", "데이터가 없습니다", null)
    }
}
