package com.onegravity.bloc.util

import com.onegravity.bloc.TodoDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual fun driver(dbName: String): SqlDriver = NativeSqliteDriver(TodoDatabase.Schema, dbName)
