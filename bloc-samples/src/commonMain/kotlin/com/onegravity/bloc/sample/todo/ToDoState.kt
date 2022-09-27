package com.onegravity.bloc.sample.todo

data class ToDoState(
    val todos: List<ToDo> = emptyList(),
    val filter: FilterStatus = FilterStatus.All
)
