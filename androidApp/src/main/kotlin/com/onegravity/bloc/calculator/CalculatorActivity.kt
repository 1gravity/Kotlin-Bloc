package com.onegravity.bloc.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.*
import com.onegravity.bloc.databinding.ActivityCalculatorBinding

class CalculatorActivity : AppCompatActivity() {

    /**
     * If the Bloc needs access to the StateKeeper, InstanceKeeper or the BackPressedHandlerOwner
     * then we need to pass the ActivityBlocContext to the ViewModel (the `it` in the constructor).
     * If the Bloc doesn't need either of them this will do (the ViewModel needs to be changed too):
     * ```
     *    private val viewModel by viewModels<CalculatorViewModel>()
     * ```
     */
    private val viewModel by viewModel { CalculatorViewModel(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind<ActivityCalculatorBinding>(R.layout.activity_calculator) { it.viewmodel = viewModel }
    }

}