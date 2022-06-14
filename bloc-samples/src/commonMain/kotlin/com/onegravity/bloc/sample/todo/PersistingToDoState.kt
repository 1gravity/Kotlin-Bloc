package com.onegravity.bloc.sample.todo

import com.onegravity.bloc.state.BlocStateBase
import com.onegravity.bloc.util.getKoinInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PersistingToDoState(
    coroutineScope: CoroutineScope
) : BlocStateBase<List<ToDo>, List<ToDo>>(
    initialState = emptyList()
) {

    private val dao = getKoinInstance<ToDoDao>()

    init {
        coroutineScope.launch(Dispatchers.Default) {
            dao.getFlow().collect { state.send(it) }
        }
    }

    override fun send(proposal: List<ToDo>) {
        proposal.forEach { newTodo ->
            val oldTodo = value.firstOrNull { it.uuid == newTodo.uuid }
            if (newTodo != oldTodo) {
                dao.upsert(newTodo.uuid, newTodo.description, newTodo.completed)
            }
        }
    }

}
