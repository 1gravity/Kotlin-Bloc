package com.onegravity.bloc

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner

class ActivityLazy<A>(private val getOwner: () -> A) : Lazy<A>
        where A : OnBackPressedDispatcherOwner,
              A : ViewModelStoreOwner,
              A : LifecycleOwner {

    private var cached: A? = null

    override val value: A
        get() = cached ?: getOwner().also { cached = it }

    override fun isInitialized() = cached != null

}
