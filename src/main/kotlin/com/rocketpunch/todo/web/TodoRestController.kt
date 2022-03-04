package com.rocketpunch.todo.web

import com.rocketpunch.todo.domain.Todo
import com.rocketpunch.todo.domain.TodoRepository
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RequestMapping("/todos")
@RestController
class TodoRestController(
    private val todoRepository: TodoRepository
) {
    @ResponseStatus(CREATED)
    @PostMapping
    fun postTodos(@RequestBody dto: TodoPostRequestBody): TodoModel {
        return Todo(null, dto.name, dto.completed ?: false)
            .let { todo -> todoRepository.save(todo) }
            .let { todoSaved -> TodoModel(todoSaved) }
    }
}

data class TodoPostRequestBody(
    val name: String,
    val completed: Boolean?
)

data class TodoModel(
    val id: Long,
    val name: String,
    val completed: Boolean?,
    val completed_at: LocalDateTime?,
    val created_at: LocalDateTime,
    val updated_at: LocalDateTime
) {
    constructor(todo: Todo) : this(
        todo.id ?: -1,
        todo.name,
        todo.completed,
        todo.completedAt,
        todo.createdAt,
        todo.updatedAt
    )
}