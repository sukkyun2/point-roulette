package org.example.roulette.api.common.api

data class ApiResponse<out T>(
    val code: String,
    val message: String?,
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
