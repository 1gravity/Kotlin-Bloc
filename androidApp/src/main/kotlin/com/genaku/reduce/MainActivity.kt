package com.genaku.reduce

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.genaku.reduce.books.BooksActivity
import com.genaku.reduce.counter.CounterActivity
import com.genaku.reduce.counter.CounterReduxActivity
import com.genaku.reduce.databinding.ActivityMainBinding
import com.genaku.reduce.traffic.TrafficActivity
import com.onegravity.knot.activityKnotContext
import com.onegravity.knot.sample.MainMenu.State.*
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels {
        viewModelFactory { MainViewModel(activityKnotContext()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind<ActivityMainBinding>(R.layout.activity_main) { it.viewmodel = viewModel }

        lifecycleScope.launch {
            viewModel.state.collect { state ->
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
