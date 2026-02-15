package org.example.roulette.config

import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import org.example.roulette.api.common.api.SwaggerApiResponse
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class SwaggerConfig {

    @Bean
    fun swaggerCustomizer(): OperationCustomizer {
        return OperationCustomizer { operation, handlerMethod ->
            val annotation =
                handlerMethod.getMethodAnnotation(SwaggerApiResponse::class.java)

            if (annotation != null) {
                operation.responses.addApiResponse(
                    "200",
                    ApiResponse()
                        .description(annotation.description)
                        .content(
                            Content().addMediaType(
                                "application/json",
                                MediaType().schema(
                                    Schema<Any>()
                                        .`$ref`("#/components/schemas/${annotation.schema.simpleName}")
                                )
                            )
                        )
                )
            }
            operation
        }
    }

}