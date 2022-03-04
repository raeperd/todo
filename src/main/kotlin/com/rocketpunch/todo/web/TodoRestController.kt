package com.rocketpunch.todo.web

import com.rocketpunch.todo.domain.Todo
import com.rocketpunch.todo.domain.TodoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
    fun postTodos(@RequestParam apikey: String, @RequestBody dto: TodoUpdateRequestBody): TodoModel {
        return Todo(null, dto.name, dto.completed ?: false)
            .let { todo -> todoRepository.save(todo) }
            .let { todoSaved -> TodoModel(todoSaved) }
    }

    @GetMapping("/{id}")
    fun getTodosById(@PathVariable id: Long): TodoModel {
        return todoRepository.findByIdOrThrow(id)
            .let { todo -> TodoModel(todo) }
    }

    @GetMapping
    fun getTodos(): List<TodoSummarizedModel> {
        return todoRepository.findAll()
            .map { todo -> TodoSummarizedModel(todo) }
    }

    @PutMapping("/{id}")
    fun putTodosById(@PathVariable id: Long, @RequestBody dto: TodoUpdateRequestBody): TodoModel {
        return todoRepository.findByIdOrThrow(id)
            .let { todo ->
                todo.name = dto.name
                todo.completed = dto.completed ?: false
                return@let todoRepository.save(todo)
            }.let { todoUpdated -> TodoModel(todoUpdated) }
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/{id}")
    fun deleteTodoById(@PathVariable id: Long) {
        todoRepository.findByIdOrThrow(id)
            .let { todoRepository.delete(it) }
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NoSuchElementException::class)
    fun handleNoSuchElementException(exception: NoSuchElementException): ExceptionModel {
        return ExceptionModel("404", exception.localizedMessage)
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    fun handleException(exception: Exception): ExceptionModel {
        return ExceptionModel("500", exception.localizedMessage)
    }

    private fun TodoRepository.findByIdOrThrow(id: Long): Todo {
        return findByIdOrNull(id) ?: throw NoSuchElementException("No such todo with given id: $id")
    }
}

data class TodoUpdateRequestBody(
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

data class TodoSummarizedModel(
    val id: Long,
    val name: String
) {
    constructor(todo: Todo) : this(todo.id ?: -1, todo.name)
}

data class ExceptionModel(
    val status: String,
    val message: String
)