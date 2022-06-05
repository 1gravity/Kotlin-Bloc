package com.onegravity.bloc.todo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.R
import com.onegravity.bloc.compose.observeState
import com.onegravity.bloc.sample.todo.*

@Composable
fun ToDoInput(bloc: Bloc<List<ToDo>, ToDoAction, Unit>) {
    val state: List<ToDo> by bloc.observeState()

    var text: String by rememberSaveable { mutableStateOf("") }

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
        )
    )
}
