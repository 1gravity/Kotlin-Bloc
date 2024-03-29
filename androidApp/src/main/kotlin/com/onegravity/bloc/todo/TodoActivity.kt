package com.onegravity.bloc.todo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.sample.todo.toDoBloc
import com.onegravity.bloc.util.ComposeAppTheme
import org.koin.android.ext.android.get

class TodoActivity : AppCompatActivity() {

    private val bloc by getOrCreate { toDoBloc(it, get()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeAppTheme {
                ToDoUi(bloc)
            }
        }
    }

}
