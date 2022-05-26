package com.onegravity.bloc.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.R
import com.onegravity.bloc.compose.observeState
import com.onegravity.bloc.posts_compose.*
import com.onegravity.bloc.sample.todo.AddToDo
import com.onegravity.bloc.sample.todo.ToDo
import com.onegravity.bloc.sample.todo.ToDoAction

@Composable
fun RootUi(bloc: Bloc<List<ToDo>, ToDoAction, Unit>) {
    val state: List<ToDo> by bloc.observeState()

    var text by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp)) {

        TextField(
            value = text,
            label = { Text(stringResource(R.string.todo_description)) },
            maxLines = 1,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Blue, fontWeight = FontWeight.Bold),
            onValueChange = { text = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    bloc.send(AddToDo(text))
                    text = ""
                },
            ),
        )

        Spacer(Modifier.height(24.0.dp))

        ToDoList(bloc)
    }

}
