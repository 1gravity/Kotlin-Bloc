package com.onegravity.bloc.compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onegravity.bloc.*
import com.onegravity.bloc.sample.posts.compose.PostsComponentImpl

class ComposeActivity : AppCompatActivity() {

    /**
     * We can either use a Bloc tied to the lifecycle of a ViewModel or make sure the Bloc retains
     * its state "manually" by using InstanceKeeper (see PostsComponentImpl).
     */
    class DummyViewModel(context: ActivityBlocContext) : ViewModel() {
        val component = PostsComponentImpl(blocContext(context))
    }
    private val viewModel by viewModels<DummyViewModel> { factory { DummyViewModel(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val component = viewModel.component
        val component = PostsComponentImpl(defaultBlocContext())

        setContent {
            ComposeAppTheme {
                RootUi(component)
            }
        }
    }

}
