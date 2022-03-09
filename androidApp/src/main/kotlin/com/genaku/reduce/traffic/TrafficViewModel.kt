package com.genaku.reduce.traffic

import androidx.lifecycle.ViewModel
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.sample.traffic.Street
import com.onegravity.knot.sample.traffic.TrafficLight

class TrafficViewModel(context: KnotContext) : ViewModel() {

    private val street1 = Street(context, 100)
    private val street2 = Street(context, 200)
    private val street3 = Street(context, 300)

    private val tl1 = TrafficLight(
        context = context,
        delay = 30,
        limit = 10
    )
    private val tl2 = TrafficLight(
        context = context,
        delay = 20,
        limit = 5
    )
    private val tl3 = TrafficLight(
        context = context,
        delay = 10
    )

    init {
        initStreets()
    }

    private fun initStreets() {
        street1.setTrafficLight(tl1)
        street2.setTrafficLight(tl2)
        street3.setTrafficLight(tl3)

        tl1.setStreetIn(street1)
        tl1.setStreetOut(street2)
        tl2.setStreetIn(street2)
        tl2.setStreetOut(street3)
        tl3.setStreetIn(street3)
        tl3.setStreetOut(street1)
    }

    val street1State
        get() = street1.state

    val street2State
        get() = street2.state

    val street3State
        get() = street3.state

    val tl1State
        get() = tl1.state

    val tl2State
        get() = tl2.state

    val tl3State
        get() = tl3.state

    fun addCar() {
        street1.carIn()
    }

}