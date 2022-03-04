package com.rocketpunch.todo.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ApiKeyInterceptor(
    methods: Collection<HttpMethod>,
    private val objectMapper: ObjectMapper
) : HandlerInterceptor {
    private val methods = methods.map { it.toString() }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.method !in methods) {
            return true
        }
        if (request.getParameter("apikey") == null) {
            response.status = UNAUTHORIZED.value()
            response.contentType = "application/json"
            response.writer.write(objectMapper.writeValueAsString(ExceptionModel("401", "Not Authorized")))
            return false
        }
        return true
    }
}