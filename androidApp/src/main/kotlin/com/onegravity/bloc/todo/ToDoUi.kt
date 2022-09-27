@file:Suppress("WildcardImport")

package com.onegravity.bloc.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.sample.todo.ToDo
import com.onegravity.bloc.sample.todo.ToDoAction

@Composable
@Suppress("FunctionNaming", "FunctionName")
fun ToDoUi(bloc: Bloc<List<ToDo>, ToDoAction, Unit>) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)) {
        ToDoInput(bloc)

        Spacer(Modifier.height(24.0.dp))

        ToDoList(bloc)
    }
}
