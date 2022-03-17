package com.onegravity.bloc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.onegravity.bloc.books.BooksActivity
import com.onegravity.bloc.counter.CounterActivity
import com.onegravity.bloc.counter.CounterReduxActivity
import com.onegravity.bloc.databinding.ActivityMainBinding
import com.onegravity.bloc.traffic.TrafficActivity
import com.onegravity.bloc.sample.MainMenu.State.*

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels {
        viewModelFactory { MainViewModel(activityBlocContext()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind<ActivityMainBinding>(R.layout.activity_main) { it.viewmodel = viewModel }

        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                Log.d("bloc", "Navigate to $state")
                when (state) {
                    ShowCounter1 -> start(CounterActivity::class.java)
                    ShowCounter2 -> start(CounterReduxActivity::class.java)
                    ShowBooks -> start(BooksActivity::class.java)
                    ShowTraffic -> start(TrafficActivity::class.java)
                    MainMenu -> { /* do nothing */ }
                }
            }
        }
    }

    private fun <A: Activity, C: Class<A>> start(clazz: C) {
        val intent = Intent(this, clazz)
        startActivity(intent)
    }

}
