package com.onegravity.bloc.sample.todo

import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.bloc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

fun toDoBloc(context: BlocContext) = bloc<ToDoState, ToDoAction>(
    context = context,
//    blocState = blocState(ToDoState())                                 // non persisting BlocState
    blocState = PersistingToDoState(CoroutineScope(SupervisorJob()))     // persisting BlocState
) {
    reduce<AddToDo> {
        val todo = ToDo(description = action.description)
        state.copy(todos = state.todos.toMutableList().apply { add(todo) })
    }

    reduce<UpdateToDo> {
        val index = state.todos.indexOfFirst { it.uuid == action.id }
        if (index == -1) state else state.copy(todos = state.todos.toMutableList().apply {
            set(index, state.todos[index].copy(description = action.description))
        })
    }

    reduce<ToggleToDo> {
        val index = state.todos.indexOfFirst { it.uuid == action.id }
        if (index == -1) state else state.copy(todos = state.todos.toMutableList().apply {
            set(index, state.todos[index].copy(completed = !state.todos[index].completed))
        })
    }

    reduce<RemoveToDo> {
        val index = state.todos.indexOfFirst { it.uuid == action.id }
        if (index == -1) state else state.copy(
            todos = state.todos.toMutableList().apply { remove(state.todos[index]) }
        )
    }

    reduce<SetFilter> {
        state.copy(filter = action.filter)
    }

}
