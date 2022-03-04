package com.rocketpunch.todo.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TodoTest {

    @Test
    fun `when change name with same value expect not updated`() {
        val todo = Todo(null, "todo")

        todo.name = todo.name

        assertThat(todo.updatedAt).isEqualTo(todo.createdAt)
    }

    @Test
    fun `when change name expect updatedAt changed`() {
        val todo = Todo(null, "todo")

        todo.name = "new name"

        assertThat(todo.updatedAt).isAfter(todo.createdAt)
    }

    @Test
    fun `when change completed with same value expect not updated`() {
        val todo = Todo(null, "todo", completed = false)

        todo.completed = false

        assertThat(todo.updatedAt).isEqualTo(todo.createdAt)
    }

    @Test
    fun `when complete undone todo expect completedAt updated`() {
        val todo = Todo(null, "todo", completed = false)

        assertThat(todo.completedAt).isNull()
        todo.completed = true

        assertThat(todo.completedAt).isAfter(todo.createdAt)
    }

    @Test
    fun `when complete undone todo expect updatedAt equalsTo completedAt`() {
        val todo = Todo(null, "todo", completed = false)

        todo.completed = true

        assertThat(todo.updatedAt).isEqualTo(todo.completedAt)
            .isAfter(todo.createdAt)
    }

    @Test
    fun `when undone done todo expect completedAt is null`() {
        val todo = Todo(null, "todo", completed = true)

        assertThat(todo.completedAt).isNotNull
        todo.completed = false

        assertThat(todo.completedAt).isNull()
    }
}