package com.onegravity.bloc.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.defaultBlocContext
import com.onegravity.bloc.sample.posts.compose.PostsComponentImpl

class ComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = defaultBlocContext { PostsComponentImpl(this) }

        setContent {
            ComposeAppTheme {
                RootUi(component)
            }
        }
    }

}
