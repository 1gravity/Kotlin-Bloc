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

        // Using getOrCreate we can create any "component" that takes the ActivityBlocContext as
        // parameter, e.g a simple Bloc:
        // val bloc = getOrCreate { bloc<Int, Int>(it, 1) }
        // Note: this creates a ViewModel is created under the hood to have the "correct" lifecycle
        // (not the Activity lifecycle)

        setContent {
            ComposeAppTheme {
                RootUi(component)
            }
        }
    }

}
