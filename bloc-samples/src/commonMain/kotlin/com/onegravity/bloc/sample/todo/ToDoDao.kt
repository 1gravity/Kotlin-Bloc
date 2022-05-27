package com.onegravity.bloc.sample.todo

import com.onegravity.bloc.TodoDatabase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class ToDoDao(private val db: TodoDatabase) {
    private val queries = db.todoQueries

    private val mapper = { id: Long,
                           createdAt: Instant,
                           modifiedAt: Instant,
                           uuid: String,
                           description: String,
                           completed: Boolean ->
        ToDo(uuid, description, completed)
    }

    fun get(): List<ToDo> = queries.selectAll(mapper).executeAsList()

    fun getFlow(): Flow<List<ToDo>> = queries.selectAll(mapper).asFlow().mapToList()

    fun upsert(uuid: String, description: String, completed: Boolean): Long {
        var rowId = 0L
        val now = Clock.System.now()
        db.transaction {
            val todo = queries.selectByUuid(uuid).executeAsOneOrNull()
            rowId = if (todo == null) {
                queries.insert(now, now, uuid, description, completed)
                queries.selectLastInsertedRowId().executeAsOne()
            } else {
                update(todo.id, description, completed)
            }
        }
        return rowId
    }

    private fun update(id: Long, description: String, completed: Boolean): Long {
        var rowId = 0L
        val now = Clock.System.now()
        db.transaction {
            queries.updateById(now, description, completed, id)
            rowId = queries.selectLastInsertedRowId().executeAsOne()
        }
        return rowId
    }
}
