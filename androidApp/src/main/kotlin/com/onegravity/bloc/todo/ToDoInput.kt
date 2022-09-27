@file:Suppress("WildcardImport")

package com.onegravity.bloc.todo

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.R
import com.onegravity.bloc.sample.todo.AddToDo
import com.onegravity.bloc.sample.todo.ToDo
import com.onegravity.bloc.sample.todo.ToDoAction

@Composable
@Suppress("FunctionNaming", "FunctionName")
fun ToDoInput(bloc: Bloc<List<ToDo>, ToDoAction, Unit>) {
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
