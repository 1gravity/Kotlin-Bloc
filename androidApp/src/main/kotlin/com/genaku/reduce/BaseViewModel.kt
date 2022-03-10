package com.genaku.reduce

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.defaultKnotContext

open class BaseViewModel(context: ActivityKnotContext) : ViewModel(), LifecycleOwner {

    @SuppressLint("StaticFieldLeak")
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle() = lifecycleRegistry

    val viewModelContext = defaultKnotContext(context, lifecycleRegistry)
        .also { lifecycleRegistry.currentState = Lifecycle.State.CREATED }

    override fun onCleared() {
        super.onCleared()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

}