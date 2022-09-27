package com.onegravity.bloc

import androidx.lifecycle.ViewModel
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.create
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.start
import com.arkivanov.essenty.lifecycle.stop

/**
 * A ViewModel with an Essenty lifecycle
 */
internal class BlocViewModel : ViewModel() {
    val lifecycleRegistry = LifecycleRegistry()
    val instanceKeeper = InstanceKeeperDispatcher()

    init {
        lifecycleRegistry.create()
        lifecycleRegistry.start()
    }

    override fun onCleared() {
        lifecycleRegistry.stop()
        lifecycleRegistry.destroy()
        instanceKeeper.destroy()
    }
}
