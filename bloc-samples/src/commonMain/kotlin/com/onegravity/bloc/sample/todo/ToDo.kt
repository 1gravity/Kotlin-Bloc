package com.onegravity.bloc.sample.todo

import com.benasher44.uuid.uuid4

data class ToDo(
    val uuid: String = uuid4().toString(),
    val description: String = "",
    val completed: Boolean = false
)
