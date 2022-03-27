package com.onegravity.bloc

import androidx.lifecycle.*
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.context.BlocContextOwner
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class BlocContextOwnerImpl(viewModel: ViewModel, context: ActivityBlocContext) :
    BlocContextOwner, LifecycleOwner {

    override fun getLifecycle() = lifecycleRegistry

    private val lifecycleRegistry = LifecycleRegistry(this)

    override val blocContext: BlocContext = context.toBlocContext(lifecycleRegistry)

    init {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED

        viewModel.viewModelScope.launch {
            while (isActive) { delay(Long.MAX_VALUE) }
        }.invokeOnCompletion {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }
    }

}

