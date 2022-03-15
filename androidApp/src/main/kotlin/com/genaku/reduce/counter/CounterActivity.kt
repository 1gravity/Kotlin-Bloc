package com.genaku.reduce.counter

import android.os.Bundle
import androidx.activity.viewModels
import com.genaku.reduce.BaseActivity
import com.genaku.reduce.R
import com.genaku.reduce.databinding.ActivityCounterSimpleBinding
import com.onegravity.knot.activityKnotContext

class CounterActivity : BaseActivity() {

    private val viewModel: CounterSimpleViewModel by viewModels {
        viewModelFactory { CounterSimpleViewModel(activityKnotContext()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind<ActivityCounterSimpleBinding>(R.layout.activity_counter_simple) { it.viewmodel = viewModel }
    }

}