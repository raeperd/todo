package com.rocketpunch.todo.domain

import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class Todo(
    @Id val id: Long? = null,
    val name: String,
    val completed: Boolean = false,
    val completedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime = now(),
    val updatedAt: LocalDateTime = createdAt
)