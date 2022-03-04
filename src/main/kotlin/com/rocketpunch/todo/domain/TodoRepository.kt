package com.rocketpunch.todo.domain

import org.springframework.data.repository.CrudRepository

interface TodoRepository : CrudRepository<Todo, Long> {
}