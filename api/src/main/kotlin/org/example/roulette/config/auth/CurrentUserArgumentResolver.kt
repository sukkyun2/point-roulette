package org.example.roulette.config.auth

import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import jakarta.servlet.http.HttpServletRequest

@Component
class CurrentUserArgumentResolver : HandlerMethodArgumentResolver {

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(CurrentUser::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.nativeRequest as HttpServletRequest
        
        val userId = request.getAttribute("userId") as? Long 
            ?: throw IllegalStateException("User ID not found in request attributes")
        val nickname = request.getAttribute("nickname") as? String 
            ?: throw IllegalStateException("Nickname not found in request attributes")
        
        return when (parameter.parameterType) {
            SimpleUser::class.java -> SimpleUser(userId, nickname)
            Long::class.java -> userId
            else -> throw IllegalArgumentException("Unsupported parameter type: ${parameter.parameterType}")
        }
    }
}