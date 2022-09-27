@file:Suppress("WildcardImport")

package com.onegravity.bloc.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.compose.observeState
import com.onegravity.bloc.sample.todo.ToDo
import com.onegravity.bloc.sample.todo.ToDoAction
import com.onegravity.bloc.sample.todo.ToggleToDo

@Composable
@Suppress("FunctionNaming", "FunctionName")
fun ToDoList(bloc: Bloc<List<ToDo>, ToDoAction, Unit>) {
    val state: List<ToDo> by bloc.observeState()

    var filter by remember { mutableStateOf(FilterStatus.All) }

    val regular = TextStyle(color = MaterialTheme.colors.primaryVariant)
    val selected =
        TextStyle(color = MaterialTheme.colors.secondaryVariant, fontWeight = FontWeight.Bold)

    Row(Modifier.fillMaxWidth()) {
        Text(
            "${state.filter { !it.completed }.size} items left",
            color = MaterialTheme.colors.primaryVariant
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            ClickableText(
                text = AnnotatedString("All"),
                modifier = Modifier.padding(end = 16.dp),
                onClick = {
                    filter = FilterStatus.All
                },
                style = if (filter == FilterStatus.All) selected else regular
            )
            ClickableText(
                text = AnnotatedString("Active"),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                onClick = {
                    filter = FilterStatus.Active
                },
                style = if (filter == FilterStatus.Active) selected else regular
            )
            ClickableText(
                text = AnnotatedString("Completed"),
                modifier = Modifier.padding(start = 16.dp),
                onClick = {
                    filter = FilterStatus.Completed
                },
                style = if (filter == FilterStatus.Completed) selected else regular
            )
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()) {
        LazyColumn {
            val toTos = state.filter {
                when (filter) {
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
