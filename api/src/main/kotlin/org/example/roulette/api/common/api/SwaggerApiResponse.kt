package org.example.roulette.api.common.api

import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SwaggerApiResponse(
    val description: String = "성공",
    val schema: KClass<*>
)
