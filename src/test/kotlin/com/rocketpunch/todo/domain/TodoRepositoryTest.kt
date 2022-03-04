package com.rocketpunch.todo.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest

@DataJdbcTest
class TodoRepositoryTest {

    @Autowired
    private lateinit var todoRepository: TodoRepository

    @Test
    fun `when save todo expect to be persisted`() {
        val todoSaved = todoRepository.save(Todo(null, "", false))

        assertThat(todoSaved.id).isNotNull
    }
}