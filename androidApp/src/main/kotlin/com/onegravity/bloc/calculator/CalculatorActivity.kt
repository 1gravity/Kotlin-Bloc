package com.onegravity.bloc.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onegravity.bloc.R
import com.onegravity.bloc.bind
import com.onegravity.bloc.databinding.ActivityCalculatorBinding
import com.onegravity.bloc.viewModel

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

        // todo remove this, but make an example on how to use blocs from Activities directly
        //      also this will crash the app since the ViewModel creates the Bloc as well:
        //      SavedStateProvider with the given key is already registered
//        val bloc = defaultBlocContext { bloc() }

        bind<ActivityCalculatorBinding>(R.layout.activity_calculator) { it.viewmodel = viewModel }
    }

}