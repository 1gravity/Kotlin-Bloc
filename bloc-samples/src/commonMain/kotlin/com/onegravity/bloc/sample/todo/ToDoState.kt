package com.onegravity.bloc.sample.todo

import com.benasher44.uuid.uuid4
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.bloc

data class ToDo(
    val id: String = uuid4().toString(),
    val description: String = "",
    val completed: Boolean = false
)

sealed class ToDoAction(open val id: String?)
data class AddToDo(val description: String) : ToDoAction(null)
data class UpdateToDo(override val id: String, val description: String) : ToDoAction(id)
data class ToggleToDo(override val id: String) : ToDoAction(id)
data class RemoveToDo(override val id: String) : ToDoAction(id)

fun toToBloc(context: BlocContext) = bloc<List<ToDo>, ToDoAction>(context, emptyList()) {
    reduce<AddToDo> {
        val todo = ToDo(description = action.description)
        state.toMutableList().apply { add(todo) }
    }

    reduce<UpdateToDo> {
        val index = state.indexOfFirst { it.id == action.id }
        if (index == -1) state else state.toMutableList().apply {
            set(index, state[index].copy(description = action.description))
        }
    }

    reduce<ToggleToDo> {
        val index = state.indexOfFirst { it.id == action.id }
        if (index == -1) state else state.toMutableList().apply {
            set(index, state[index].copy(completed = ! state[index].completed))
        }
    }

    reduce<RemoveToDo> {
        val index = state.indexOfFirst { it.id == action.id }
        if (index == -1) state else state.toMutableList().apply { remove(state[index]) }
    }

    // we can do above in a single reduce block
    reduce {
        val index = state.indexOfFirst { it.id == action.id }
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
