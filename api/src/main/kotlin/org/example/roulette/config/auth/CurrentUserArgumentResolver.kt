package org.example.roulette.config.auth

import jakarta.servlet.http.HttpServletRequest
import org.example.roulette.api.user.domain.UserQueryService
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class CurrentUserArgumentResolver(
    private val userQueryService: UserQueryService,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        parameter.hasParameterAnnotation(CurrentUser::class.java)

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any? {
        val request = webRequest.nativeRequest as HttpServletRequest

        val userId =
            request.getAttribute("userId") as? Long
                ?: throw IllegalStateException("User ID not found in request attributes")
        val nickname =
            request.getAttribute("nickname") as? String
                ?: throw IllegalStateException("Nickname not found in request attributes")

        return when (parameter.parameterType) {
            SimpleUser::class.java -> {
                val user =
                    userQueryService.findById(userId)
                        ?: throw IllegalStateException("User not found with ID: $userId")
                SimpleUser(userId, nickname, user.balance)
            }
            Long::class.java -> userId
            else -> throw IllegalArgumentException("Unsupported parameter type: ${parameter.parameterType}")
        }
    }
}
