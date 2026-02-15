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
        return OpenAPI()
            .info(
                Info()
                    .title("Roulette API")
                    .description("룰렛 게임 API 문서")
                    .version("1.0.0")
            )
            .addSecurityItem(
                SecurityRequirement().addList("BearerAuth")
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "BearerAuth",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .description("JWT 토큰을 입력하세요")
                    )
            )
    }

    @Bean
    fun swaggerCustomizer(): OperationCustomizer =
        OperationCustomizer { operation, handlerMethod ->
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
                                        .`$ref`("#/components/schemas/${annotation.schema.simpleName}"),
                                ),
                            ),
                        ),
                )
            }
            operation
        }
}
