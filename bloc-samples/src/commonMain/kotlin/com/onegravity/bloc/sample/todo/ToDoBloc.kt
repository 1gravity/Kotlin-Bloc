package com.onegravity.bloc.sample.todo

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

fun toDoBloc(context: BlocContext) = bloc<List<ToDo>, ToDoAction>(
    context = context,
    blocState = ToDoState(CoroutineScope(SupervisorJob()))
) {
    reduce<AddToDo> {
        val todo = ToDo(description = action.description)
        state.toMutableList().apply { add(todo) }
    }

    reduce<UpdateToDo> {
        val index = state.indexOfFirst { it.uuid == action.id }
        if (index == -1) state else state.toMutableList().apply {
            set(index, state[index].copy(description = action.description))
        }
    }

    reduce<ToggleToDo> {
        val index = state.indexOfFirst { it.uuid == action.id }
        if (index == -1) state else state.toMutableList().apply {
            set(index, state[index].copy(completed = ! state[index].completed))
        }
    }

    reduce<RemoveToDo> {
        val index = state.indexOfFirst { it.uuid == action.id }
        if (index == -1) state else state.toMutableList().apply { remove(state[index]) }
    }

    // we can do above in a single reduce block
    reduce {
        val index = state.indexOfFirst { it.uuid == action.id }
        when {
            index >= 0 && action is UpdateToDo -> state.toMutableList().apply {
                set(index, state[index].copy(description = (action as UpdateToDo).description))
            }
            index >= 0 && action is ToggleToDo -> state.toMutableList().apply {
                set(index, state[index].copy(completed = ! state[index].completed))
            }
            index >= 0 && action is RemoveToDo -> state.toMutableList().apply { remove(state[index]) }
            else -> {
                val todo = ToDo(description = (action as AddToDo).description)
                state.toMutableList().apply { add(todo) }
            }
        }
    }

}
