package com.rocketpunch.todo.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.DELETE
import org.springframework.http.HttpMethod.POST
import org.springframework.http.HttpMethod.PUT
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration(objectMapper: ObjectMapper) : WebMvcConfigurer {

    private val authenticationInterceptor = ApiKeyInterceptor(
        listOf(POST, PUT, DELETE), objectMapper
    )

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authenticationInterceptor)
            .addPathPatterns("/todos/{\\d+}")
    }
}