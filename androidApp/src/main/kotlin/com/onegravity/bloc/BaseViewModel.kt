package com.onegravity.bloc

import androidx.lifecycle.*
import com.onegravity.knot.ActivityBlocContext
import com.onegravity.knot.defaultBlocContext

open class BaseViewModel(context: ActivityBlocContext) : ViewModel(), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    override fun getLifecycle() = lifecycleRegistry

    protected val viewModelContext = defaultBlocContext(context, lifecycleRegistry)
        .also { lifecycleRegistry.currentState = Lifecycle.State.CREATED }

    override fun onCleared() {
        super.onCleared()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

}
