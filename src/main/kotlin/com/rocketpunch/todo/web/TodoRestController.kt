package com.rocketpunch.todo.web

import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.LocalDateTime.now

@RequestMapping("/todos")
@RestController
class TodoRestController {

    @ResponseStatus(CREATED)
    @PostMapping
    fun postTodos(@RequestBody dto: TodoPostRequestBody): TodoModel {
        return TodoModel(1, dto.name, dto.completed, null, now(), now())
    }
}

data class TodoPostRequestBody(
    val name: String,
    val completed: Boolean?
)

data class TodoModel(
    val id: Int,
    val name: String,
    val completed: Boolean?,
    val completed_at: LocalDateTime?,
    val created_at: LocalDateTime,
    val updated_at: LocalDateTime
)