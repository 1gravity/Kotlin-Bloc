package com.onegravity.bloc.menuCompose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.sample.MainMenuCompose.bloc
import com.onegravity.bloc.util.ComposeAppTheme

class MainActivityCompose : AppCompatActivity() {

    private val bloc by getOrCreate { bloc(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeAppTheme {
                RootUi(bloc)
            }
        }
    }

}
