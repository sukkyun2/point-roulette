package org.example.roulette.api.common.api

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException

@RestControllerAdvice
class CommonExceptionHandler {
    private val logger = LoggerFactory.getLogger(CommonExceptionHandler::class.java)

    @ExceptionHandler(NoHandlerFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun noHandlerFoundException(ex: NoHandlerFoundException): ApiResponse<Void?> {
        logger.warn(ex.message)

        return ApiResponse.badRequest(ex.message)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(ex: Exception): ApiResponse<Void?> {
        logger.error(ex.message, ex)

        return ApiResponse.error("서버 오류가 발생했습니다: ${ex.message}")
    }
}
