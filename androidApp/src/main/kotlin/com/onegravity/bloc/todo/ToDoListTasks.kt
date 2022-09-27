@file:Suppress("WildcardImport")

package com.onegravity.bloc.todo

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.compose.observeState
import com.onegravity.bloc.sample.todo.FilterStatus
import com.onegravity.bloc.sample.todo.ToDoAction
import com.onegravity.bloc.sample.todo.ToDoState
import com.onegravity.bloc.sample.todo.ToggleToDo

@Composable
@Suppress("FunctionNaming", "FunctionName")
fun ToDoListTasks(bloc: Bloc<ToDoState, ToDoAction, Unit>) {
    val state: ToDoState by bloc.observeState()

    Row(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
        LazyColumn {
            val toTos = state.todos.filter {
                when (state.filter) {
                    FilterStatus.Active -> !it.completed
                    FilterStatus.Completed -> it.completed
                    else -> true
                }
            }
            items(toTos) { todo ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = todo.completed,
                        onCheckedChange = { bloc.send(ToggleToDo(todo.uuid)) }
                    )
                    Text(
                        text = todo.description,
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        textAlign = TextAlign.Left,
                        color = MaterialTheme.colors.primaryVariant
                    )
                }
            }
        }
    }

}
