package com.genaku.reduce.counter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.genaku.reduce.BaseActivity
import com.genaku.reduce.R
import com.genaku.reduce.databinding.ActivityCounterReduxBinding
import com.onegravity.knot.activityKnotContext

class CounterReduxActivity : BaseActivity() {

    private val viewModel: CounterReduxViewModel by viewModels {
        viewModelFactory { CounterReduxViewModel(activityKnotContext()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityCounterReduxBinding>(this, R.layout.activity_counter_redux)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

}