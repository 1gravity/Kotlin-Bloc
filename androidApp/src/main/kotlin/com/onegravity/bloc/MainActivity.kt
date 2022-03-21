package com.onegravity.bloc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.onegravity.bloc.books.BooksActivity
import com.onegravity.bloc.calculator.CalculatorActivity
import com.onegravity.bloc.counter.CounterActivity
import com.onegravity.bloc.counter.CounterReduxActivity
import com.onegravity.bloc.databinding.ActivityMainBinding
import com.onegravity.bloc.posts.PostsActivity
import com.onegravity.bloc.sample.MainMenu.ActionState as NavigationTarget
import com.onegravity.bloc.sample.MainMenu.ActionState.*

class MainActivity : BaseActivity() {

    private val viewModel by viewModels<MainViewModel> { factory { MainViewModel(it) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind<ActivityMainBinding>(R.layout.activity_main) { it.viewmodel = viewModel }

        viewModel.subscribe(sideEffect = ::navigateTo)
    }

    private fun navigateTo(target: NavigationTarget) {
        when (target) {
            Counter1 -> start(CounterActivity::class.java)
            Counter2 -> start(CounterReduxActivity::class.java)
            Books -> start(BooksActivity::class.java)
            Calculator -> start(CalculatorActivity::class.java)
            Posts -> start(PostsActivity::class.java)
            MainMenu -> { /* do nothing */ }
        }
    }

    private fun <A: Activity, C: Class<A>> start(clazz: C) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

}
