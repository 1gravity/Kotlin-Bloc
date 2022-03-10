package com.genaku.reduce.traffic

import androidx.lifecycle.*
import com.onegravity.knot.context.DefaultKnotContext
import com.onegravity.knot.context.KnotContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.knot.sample.traffic.Traffic

class TrafficViewModel(val context: KnotContext) : ViewModel() {

    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry()

    private val viewModelContext = DefaultKnotContext(
        lifecycle = lifecycleRegistry,
        coroutineScope = viewModelScope,
        stateKeeper = context.stateKeeper,
        instanceKeeper = context.instanceKeeper,
        backPressedHandler = context.backPressedHandler
    )

    private val traffic = Traffic(viewModelContext)

    init {
        lifecycleRegistry.onCreate()
    }

    override fun onCleared() {
        super.onCleared()
        lifecycleRegistry.onDestroy()
    }

    val street1State
        get() = traffic.street1.state

    val street2State = traffic.street2.state

    val street3State = traffic.street3.state

    val tl1State = traffic.tl1.state

    val tl2State = traffic.tl2.state

    val tl3State = traffic.tl3.state

    fun addCar() {
        traffic.addCar()
    }

}