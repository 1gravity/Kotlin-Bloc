package com.genaku.reduce.counter

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.genaku.reduce.BaseActivity
import com.genaku.reduce.databinding.ActivityCounterBinding
import com.onegravity.knot.defaultKnotContext
import kotlinx.coroutines.launch

class CounterActivity : BaseActivity() {

    private val viewModel: CounterViewModel by viewModels {
        viewModelFactory { CounterViewModel(defaultKnotContext()) }
    }

    private lateinit var binding: ActivityCounterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCounterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindViews()
    }

    private fun bindViews() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.counter.text = "Counter: $state"
            }
        }

        binding.increment.setOnClickListener { viewModel.increment() }
        binding.decrement.setOnClickListener { viewModel.decrement() }
    }

}