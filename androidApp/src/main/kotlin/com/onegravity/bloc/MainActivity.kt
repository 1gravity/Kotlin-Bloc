package com.onegravity.bloc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.books.BooksActivity
import com.onegravity.bloc.calculator.CalculatorActivity
import com.onegravity.bloc.compose.ComposeActivity
import com.onegravity.bloc.counter.CounterActivity
import com.onegravity.bloc.counter.CounterReduxActivity
import com.onegravity.bloc.databinding.ActivityMainBinding
import com.onegravity.bloc.posts.PostActivity
import com.onegravity.bloc.sample.MainMenu.ActionState.*

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModel { MainViewModel(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind<ActivityMainBinding>(R.layout.activity_main) { it.viewmodel = viewModel }
    }

    override fun onResume() {
        super.onResume()
        viewModel.subscribe(this, sideEffect = { mapping[it]?.start() })
    }

    private val mapping = mapOf(
        Counter1 to CounterActivity::class.java,
        Counter2 to CounterReduxActivity::class.java,
        Books to BooksActivity::class.java,
        Calculator to CalculatorActivity::class.java,
        Posts to PostActivity::class.java,
        Compose to ComposeActivity::class.java
    )

    private fun <A: Activity, C: Class<A>> C.start() {
        val intent = Intent(this@MainActivity, this)
        startActivity(intent)
    }

}