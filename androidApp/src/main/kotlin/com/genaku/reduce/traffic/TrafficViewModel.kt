package com.genaku.reduce.traffic

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.genaku.reduce.BaseViewModel
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

    val street2State = traffic.street2.state

    val street3State = traffic.street3.state

    val tl1State = traffic.tl1.state

    val tl2State = traffic.tl2.state

    val tl3State = traffic.tl3.state

    fun addCar() {
        traffic.addCar()
    }

}