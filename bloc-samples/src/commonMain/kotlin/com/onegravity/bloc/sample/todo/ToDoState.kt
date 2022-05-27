package com.onegravity.bloc.sample.todo

import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.util.getKoinInstance
import com.onegravity.bloc.utils.MutableStateStream
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

// todo make this a generic persistent BlocState, eventually with pluggable persistence layer
// todo then also create a plugin for a redux store
class ToDoState(coroutineScope: CoroutineScope, initialValue: List<ToDo> = emptyList()) :
    BlocState<List<ToDo>, List<ToDo>>() {

    private val dao = getKoinInstance<ToDoDao>()

    init {
        coroutineScope.launch(Dispatchers.Default) {
            dao.getFlow().collect { todos ->
                state.send(todos)
            }
        }
    }

    private val state: MutableStateStream<List<ToDo>> = MutableStateStream(initialValue)

    override val value: List<ToDo>
        get() = state.value

    override suspend fun collect(collector: FlowCollector<List<ToDo>>) {
        state.collect(collector)
    }

    override fun send(proposal: List<ToDo>) {
        proposal.forEach { newTodo ->
            val oldTodo = state.value.firstOrNull { it.uuid == newTodo.uuid }
            if (newTodo != oldTodo) {
                dao.upsert(newTodo.uuid, newTodo.description, newTodo.completed)
            }
        }
    }

}
