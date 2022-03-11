package com.genaku.reduce

import androidx.lifecycle.*
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.defaultKnotContext

open class BaseViewModel(context: ActivityKnotContext) : ViewModel(), LifecycleOwner {

    private val lifecycleRegistry by lazy { LifecycleRegistry(this) }

    override fun getLifecycle() = lifecycleRegistry

    protected val viewModelContext = defaultKnotContext(context, lifecycleRegistry)
        .also { lifecycleRegistry.currentState = Lifecycle.State.CREATED }

    override fun onCleared() {
        super.onCleared()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

}
