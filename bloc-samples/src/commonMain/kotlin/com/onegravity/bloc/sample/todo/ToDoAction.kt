package com.onegravity.bloc.sample.todo

sealed class ToDoAction(open val id: String?)

data class AddToDo(val description: String) : ToDoAction(null)
data class UpdateToDo(override val id: String, val description: String) : ToDoAction(id)
data class ToggleToDo(override val id: String) : ToDoAction(id)
data class RemoveToDo(override val id: String) : ToDoAction(id)
