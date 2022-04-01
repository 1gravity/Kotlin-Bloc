package com.onegravity.bloc.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.sample.posts.compose.PostsComponentImpl

class ComposeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = getOrCreate { PostsComponentImpl(it) }

        setContent {
            ComposeAppTheme {
                RootUi(component)
            }
        }
    }

}
