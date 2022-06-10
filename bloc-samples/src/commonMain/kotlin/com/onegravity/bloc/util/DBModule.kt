package com.onegravity.bloc.util

import com.onegravity.bloc.TodoDatabase
import com.onegravity.bloc.sample.todo.ToDoDao
import com.onegravity.bloc.todo.Todos
import com.squareup.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant
import org.koin.dsl.module

internal val dbModule = module {
    single {
        val instantAdapter = object : ColumnAdapter<Instant, String> {
            override fun decode(databaseValue: String) = Instant.parse(databaseValue)
            override fun encode(value: Instant) = value.toString()
        }

        TodoDatabase(
            driver = driver("todo.db"),
            todosAdapter = Todos.Adapter(
                created_atAdapter = instantAdapter,
                modified_atAdapter = instantAdapter
            )
        )
    }

    single { ToDoDao(get()) }
}
