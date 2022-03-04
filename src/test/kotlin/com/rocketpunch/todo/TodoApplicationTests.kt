package com.rocketpunch.todo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rocketpunch.todo.web.TodoModel
import com.rocketpunch.todo.web.TodoPostRequestBody
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@AutoConfigureMockMvc
@SpringBootTest
class TodoApplicationTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Transactional
    @Test
    fun `when POST todos expect return created`() {
        mockMvc.postTodos("todo name", null)
            .andExpect {
                status { isCreated() }
                content {
                    jsonPath("id", greaterThan(0))
                    jsonPath("name", equalTo("todo name"))
                    jsonPath("completed", equalTo(false))
                    jsonPath("completed_at", anything())
                    jsonPath("created_at", anything())
                    jsonPath("updated_at", anything())
                }
            }
    }

    @Test
    fun `when GET todos by not exists id return not found`() {
        mockMvc.getTodosById(20)
            .andExpect {
                status { isNotFound() }
                content {
                    jsonPath("status", equalTo("404"))
                    jsonPath("message", anything())
                }
            }
    }

    @Transactional
    @Test
    fun `when GET todos by id expect return ok`() {
        val todoCreated = mockMvc.createTodos("todo", null)

        mockMvc.getTodosById(todoCreated.id)
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("id", equalTo(todoCreated.id.toInt()))
                    jsonPath("name", equalTo(todoCreated.name))
                    jsonPath("completed", equalTo(todoCreated.completed))
                    jsonPath("completed_at", equalTo(todoCreated.completed_at))
                    jsonPath("created_at", equalTo(todoCreated.created_at.toString()))
                    jsonPath("updated_at", equalTo(todoCreated.updated_at.toString()))
                }
            }
    }

    @Transactional
    @Test
    fun `when GET todos expect return todos`() {
        val todos = listOf(
            mockMvc.createTodos("todo1", null),
            mockMvc.createTodos("todo2", null)
        )

        mockMvc.get("/todos")
            .andExpect {
                status { isOk() }
                content {
                    jsonPath("$.[0].id", equalTo(todos[0].id.toInt()))
                    jsonPath("$.[1].id", equalTo(todos[1].id.toInt()))
                }
            }
    }

    @Test
    fun `when DELETE todos by id not exists expect not found status`() {
        mockMvc.delete("/todos/20")
            .andExpect {
                status { isNotFound() }
                content { jsonPath("status", equalTo("404")) }
            }
    }

    @Transactional
    @Test
    fun `when DELETE todo by id expect return no content status`() {
        val todoCreated = mockMvc.createTodos("todo", null)

        mockMvc.delete("/todos/${todoCreated.id}")
            .andExpect { status { isNoContent() } }
    }

    private fun MockMvc.createTodos(name: String, completed: Boolean?): TodoModel {
        return postTodos(name, completed).andReturn()
            .response.contentAsString
            .let { objectMapper.readValue(it) }
    }

    private fun MockMvc.postTodos(name: String, completed: Boolean?): ResultActionsDsl {
        return post("/todos?apikey=123") {
            contentType = APPLICATION_JSON
            content = objectMapper.writeValueAsString(TodoPostRequestBody(name, completed))
        }
    }

    private fun MockMvc.getTodosById(id: Long): ResultActionsDsl {
        return get("/todos/$id")
    }
}

