package com.onegravity.bloc

import androidx.lifecycle.ViewModel
import com.arkivanov.essenty.lifecycle.*

/**
 * A ViewModel with an Essenty lifecycle
 */
internal class BlocViewModel : ViewModel() {
    val lifecycleRegistry = LifecycleRegistry()

    init {
        lifecycleRegistry.create()
        lifecycleRegistry.start()
    }

    override fun onCleared() {
        lifecycleRegistry.stop()
        lifecycleRegistry.destroy()
    }
}
