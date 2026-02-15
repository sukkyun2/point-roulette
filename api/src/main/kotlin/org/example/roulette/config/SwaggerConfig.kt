package org.example.roulette.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Content
import io.swagger.v3.oas.models.media.MediaType
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.example.roulette.api.common.api.SwaggerApiResponse
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val apiResponseSchema =
            Schema<Any>().apply {
                type = "object"
                addProperty("code", Schema<Any>().type("string"))
                addProperty("message", Schema<Any>().type("string"))
                addProperty("data", Schema<Any>())
            }

        return OpenAPI()
            .info(
                Info()
                    .title("Roulette API")
                    .description("룰렛 게임 API 문서")
                    .version("1.0.0"),
            ).components(
                Components()
                    .addSchemas("ApiResponse", apiResponseSchema)
                    .addSecuritySchemes(
                        "BearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT"),
                    ),
            )
    }

    @Bean
    fun swaggerCustomizer(): OperationCustomizer =
        OperationCustomizer { operation, handlerMethod ->
            val annotation =
                handlerMethod.getMethodAnnotation(SwaggerApiResponse::class.java)

            if (annotation != null) {
                val dataRef =
                    Schema<Any>()
                        .`$ref`("#/components/schemas/${annotation.schema.simpleName}")

                val commonRef =
                    Schema<Any>()
                        .`$ref`("#/components/schemas/ApiResponse")

                val overrideData =
                    Schema<Any>().apply {
                        type = "object"
                        addProperty("data", dataRef)
                    }

                val composedSchema =
                    Schema<Any>().apply {
                        allOf = listOf(commonRef, overrideData)
                    }

                operation.responses.addApiResponse(
                    "200",
                    ApiResponse()
                        .description(annotation.description)
                        .content(
                            Content().addMediaType(
                                "application/json",
                                MediaType().schema(composedSchema),
                            ),
                        ),
                )
            }

            operation.responses.remove("500")

            operation
        }
}
