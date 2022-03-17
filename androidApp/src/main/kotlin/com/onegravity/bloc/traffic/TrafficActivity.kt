package com.onegravity.bloc.traffic

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.onegravity.bloc.BaseActivity
import com.onegravity.bloc.R
import kotlinx.coroutines.launch

class TrafficActivity : BaseActivity() {

    private val viewModel by viewModels<TrafficViewModel> { factory { TrafficViewModel(it) } }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_traffic)

        findViewById<Button>(R.id.button2).setOnClickListener {
            viewModel.addCar()
        }
        val street1 = findViewById<TextView>(R.id.tvStreet1)
        val street2 = findViewById<TextView>(R.id.tvStreet2)
        val street3 = findViewById<TextView>(R.id.tvStreet3)
        val tl1 = findViewById<TextView>(R.id.tvL1)
        val tl2 = findViewById<TextView>(R.id.tvL2)
        val tl3 = findViewById<TextView>(R.id.tvL3)
        lifecycleScope.launch {
            viewModel.street1State.collect {
                street1.text = "Street 1: $it cars"
                sumCars()
            }
        }
        lifecycleScope.launch {
            viewModel.street2State.collect {
                street2.text = "Street 2: $it cars"
                sumCars()
            }
        }
        lifecycleScope.launch {
            viewModel.street3State.collect {
                street3.text = "Street 3: $it cars"
                sumCars()
            }
        }
        lifecycleScope.launch {
            viewModel.tl1State.collect {
                tl1.text = "Traffic light 1 is $it"
            }
        }
        lifecycleScope.launch {
            viewModel.tl2State.collect {
                tl2.text = "Traffic light 2 is $it"
            }
        }
        lifecycleScope.launch {
            viewModel.tl3State.collect {
                tl3.text = "Traffic light 3 is $it"
            }
        }
    }

    private fun sumCars() {
        val cars = viewModel.street1State.value.cars
        findViewById<TextView>(R.id.tvCars).text =
            viewModel.street1State.value.cars
                .plus(viewModel.street2State.value.cars)
                .plus(viewModel.street3State.value.cars)
                .toString()
    }
}