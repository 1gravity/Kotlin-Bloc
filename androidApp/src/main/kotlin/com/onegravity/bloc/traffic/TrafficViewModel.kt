package com.onegravity.bloc.traffic

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.onegravity.bloc.BaseViewModel
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.sample.traffic.Traffic

class TrafficViewModel(val context: ActivityKnotContext) : BaseViewModel(context) {

    private val lifecycleRegistry = LifecycleRegistry()

    private val traffic = Traffic(viewModelContext)

    override fun onCleared() {
        super.onCleared()
        lifecycleRegistry.onDestroy()
    }

    val street1State
        get() = traffic.street1.state

    val street2State
        get() = traffic.street2.state

    val street3State
        get() = traffic.street3.state

    val tl1State
        get() = traffic.tl1.state

    val tl2State
        get() = traffic.tl2.state

    val tl3State
        get() = traffic.tl3.state

    fun addCar() {
        traffic.addCar()
    }

}