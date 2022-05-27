package com.onegravity.bloc.util

import com.squareup.sqldelight.db.SqlDriver

expect fun driver(dbName: String): SqlDriver
