package com.genaku.reduce.counter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.genaku.reduce.BaseActivity
import com.genaku.reduce.R
import com.genaku.reduce.databinding.ActivityCounterBinding
import com.onegravity.knot.defaultKnotContext

class CounterActivity : BaseActivity() {

    private val viewModel: CounterViewModel by viewModels {
        viewModelFactory { CounterViewModel(defaultKnotContext()) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityCounterBinding>(this, R.layout.activity_counter)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

}