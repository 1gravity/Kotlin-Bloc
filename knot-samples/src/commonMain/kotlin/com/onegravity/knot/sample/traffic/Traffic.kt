package com.onegravity.knot.sample.traffic

import com.arkivanov.essenty.lifecycle.doOnCreate
import com.onegravity.bloc.context.BlocContext

class Traffic(val context: BlocContext) {

    val street1 = Street(context, 100)
    val street2 = Street(context, 200)
    val street3 = Street(context, 300)

    val tl1 = TrafficLight(
        context = context,
        delay = 30,
        limit = 10
    )
    val tl2 = TrafficLight(
        context = context,
        delay = 20,
        limit = 5
    )
    val tl3 = TrafficLight(
        context = context,
        delay = 10
    )

    init {
        context.lifecycle.doOnCreate { initStreets() }
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

    fun addCar() {
        street1.carIn()
    }

}