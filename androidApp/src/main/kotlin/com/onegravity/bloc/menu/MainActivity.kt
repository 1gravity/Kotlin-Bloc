package com.onegravity.bloc.menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.bind
import com.onegravity.bloc.books.BooksActivity
import com.onegravity.bloc.calculator.CalculatorActivity
import com.onegravity.bloc.calculator.CalculatorActivityNoVM
import com.onegravity.bloc.counter.CounterActivity
import com.onegravity.bloc.counter.CounterActivityCompose
import com.onegravity.bloc.counter.CounterReduxActivity
import com.onegravity.bloc.databinding.ActivityMainBinding
import com.onegravity.bloc.menu_compose.MainActivityCompose
import com.onegravity.bloc.posts.PostsActivity
import com.onegravity.bloc.sample.MainMenu.ActionState.*
import com.onegravity.bloc.subscribe
import com.onegravity.bloc.todo.TodoActivity
import com.onegravity.bloc.posts_compose.PostsActivity as ComposePostsActivity

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind<ActivityMainBinding>(R.layout.activity_main) { it.viewmodel = viewModel }
    }

    override fun onResume() {
        super.onResume()
        viewModel.subscribe(this, sideEffect = { mapping[it]?.start() })
    }

    private val mapping = mapOf(
        MainMenuCompose to MainActivityCompose::class.java,
        Counter to CounterActivity::class.java,
        CounterCompose to CounterActivityCompose::class.java,
        CounterRedux to CounterReduxActivity::class.java,
        Books to BooksActivity::class.java,
        Calculator to CalculatorActivity::class.java,
        CalculatorNoVM to CalculatorActivityNoVM::class.java,
        Posts to PostsActivity::class.java,
        PostsCompose to ComposePostsActivity::class.java,
        ToDo to TodoActivity::class.java
    )

    private fun <A : Activity, C : Class<A>> C.start() {
        val intent = Intent(this@MainActivity, this)
        startActivity(intent)
    }

}
