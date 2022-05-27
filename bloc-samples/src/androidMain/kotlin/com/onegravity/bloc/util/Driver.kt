package com.onegravity.bloc.util

import com.onegravity.bloc.TodoDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

actual fun driver(dbName: String): SqlDriver = Driver().createDriver(dbName)

class Driver : KoinComponent {
    fun createDriver(dbName: String) = AndroidSqliteDriver(TodoDatabase.Schema, get(), dbName)
}

