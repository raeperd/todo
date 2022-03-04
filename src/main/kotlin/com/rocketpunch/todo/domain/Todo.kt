package com.rocketpunch.todo.domain

import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.time.LocalDateTime.now

class Todo(
    @Id val id: Long? = null,
    name: String,
    completed: Boolean = false,
    val createdAt: LocalDateTime = now(),
    updatedAt: LocalDateTime = createdAt,
    completedAt: LocalDateTime? = if (completed) createdAt else null,
) {
    var name: String = name
        set(value) {
            if (field == value) {
                return
            }
            field = value
            updatedAt = now()
        }

    var completed: Boolean = completed
        set(value) {
            if (field == value) {
                return
            }
            field = value
            updatedAt = now()
            completedAt = if (value) {
                updatedAt
            } else {
                null
            }
        }

    var completedAt: LocalDateTime? = completedAt
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    fun withId(id: Long?): Todo = Todo(id, name, completed, createdAt, updatedAt, completedAt)
}