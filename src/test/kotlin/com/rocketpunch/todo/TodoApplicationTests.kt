package com.rocketpunch.todo

import com.fasterxml.jackson.databind.ObjectMapper
import com.rocketpunch.todo.web.TodoPostRequestBody
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@AutoConfigureMockMvc
@SpringBootTest
class TodoApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `when POST todos expect return created`() {
        val requestBody = TodoPostRequestBody("todo name", null)

        mockMvc.post("/todos") {
            contentType = APPLICATION_JSON
            content = objectMapper.writeValueAsString(requestBody)
        }.andExpect {
            status { isCreated() }
            content {
                jsonPath("id", anything())
                jsonPath("name", equalTo(requestBody.name))
                jsonPath("completed", equalTo(requestBody.completed))
                jsonPath("completed_at", anything())
                jsonPath("created_at", anything())
                jsonPath("updated_at", anything())
            }
        }
    }
}
