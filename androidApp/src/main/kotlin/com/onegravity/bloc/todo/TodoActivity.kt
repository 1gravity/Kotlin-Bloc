package com.onegravity.bloc.todo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.sample.todo.toToBloc
import com.onegravity.bloc.util.ComposeAppTheme

class TodoActivity : AppCompatActivity() {

    private val bloc by getOrCreate { toToBloc(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeAppTheme {
                RootUi(bloc)
            }
        }
    }

}
