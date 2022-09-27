@file:Suppress("WildcardImport")

package com.onegravity.bloc.todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.compose.observeState
import com.onegravity.bloc.sample.todo.FilterStatus
import com.onegravity.bloc.sample.todo.SetFilter
import com.onegravity.bloc.sample.todo.ToDoAction
import com.onegravity.bloc.sample.todo.ToDoState

@Composable
@Suppress("FunctionNaming", "FunctionName")
fun ToDoListHeader(bloc: Bloc<ToDoState, ToDoAction, Unit>) {
    val state: ToDoState by bloc.observeState()

    val regular = TextStyle(color = MaterialTheme.colors.primaryVariant)
    val selected =
        TextStyle(color = MaterialTheme.colors.secondaryVariant, fontWeight = FontWeight.Bold)

    Row(Modifier.fillMaxWidth()) {
        Text(
            "${state.todos.filter { !it.completed }.size} items left",
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
                    bloc.send(SetFilter(FilterStatus.All))
                },
                style = if (state.filter == FilterStatus.All) selected else regular
            )
            ClickableText(
                text = AnnotatedString("Active"),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                onClick = {
                    bloc.send(SetFilter(FilterStatus.Active))
                },
                style = if (state.filter == FilterStatus.Active) selected else regular
            )
            ClickableText(
                text = AnnotatedString("Completed"),
                modifier = Modifier.padding(start = 16.dp),
                onClick = {
                    bloc.send(SetFilter(FilterStatus.Completed))
                },
                style = if (state.filter == FilterStatus.Completed) selected else regular
            )
        }
    }

}
