package com.onegravity.bloc.sample.todo

import com.onegravity.bloc.state.BlocStateBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersistingToDoState(
    private val dao: ToDoDao,
    coroutineScope: CoroutineScope
) : BlocStateBase<ToDoState, ToDoState>(
    initialState = ToDoState()
) {

    private var cachedState: ToDoState = ToDoState()

    init {
        coroutineScope.launch(Dispatchers.Default) {
            dao.getFlow().collect {
                cachedState = cachedState.copy(todos = it)
                state.send(ToDoState(it, cachedState.filter))
            }
        }
    }

    override fun send(proposal: ToDoState) {
        proposal.todos.forEach { newTodo ->
            val oldTodo = value.todos.firstOrNull { it.uuid == newTodo.uuid }
            if (newTodo != oldTodo) {
                dao.upsert(newTodo.uuid, newTodo.description, newTodo.completed)
            }
        }
        if (proposal.filter != cachedState.filter) {
            cachedState = cachedState.copy(filter = proposal.filter)
            state.send(cachedState)
        }
    }

}
