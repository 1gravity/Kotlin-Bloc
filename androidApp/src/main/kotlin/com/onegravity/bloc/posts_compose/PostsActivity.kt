package com.onegravity.bloc.posts_compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.getOrCreate
import com.onegravity.bloc.sample.posts.compose.PostsComponent
import com.onegravity.bloc.sample.posts.compose.PostsComponentImpl
import com.onegravity.bloc.util.ComposeAppTheme

class PostsActivity : AppCompatActivity() {

    // Using getOrCreate we can create any "component" that takes the ActivityBlocContext as
    // parameter
    // a component can be a simple Bloc:
    //   val bloc by getOrCreate { bloc<Int, Int>(it, 1) }
    // it can also be any other object that wraps around a bloc like:
    //   val component by getOrCreate { PostsComponentImpl(it) }
    // Note: a ViewModel is created under the hood to have the "correct" lifecycle
    // (not the Activity lifecycle)

    private val component: PostsComponent by getOrCreate { PostsComponentImpl(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeAppTheme {
                RootUi(component)
            }
        }
    }

}
