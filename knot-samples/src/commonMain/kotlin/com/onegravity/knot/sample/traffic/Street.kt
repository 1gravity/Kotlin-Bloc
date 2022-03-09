package com.onegravity.knot.sample.traffic

import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext
import kotlinx.coroutines.delay

data class StreetState(val cars: Int) {
    operator fun plus(cars: Int): StreetState = StreetState(this.cars + cars)
    operator fun minus(cars: Int): StreetState = StreetState(this.cars - cars)
    override fun toString() = cars.toString()
}

sealed class StreetEvent {
    object Plus : StreetEvent()
    object Minus : StreetEvent()
}

class Street(context: KnotContext, private val delay: Long) {

    private var trafficLight: TrafficLight? = null

    private val knot = knot<StreetState, StreetEvent>(context) {
        initialState = StreetState(0)
        reduce { state, event ->
            when (event) {
                StreetEvent.Plus -> state.copy(cars = state.cars + 1) + outStreet()
                StreetEvent.Minus -> when (state.cars) {
                    0 -> state.toEffect()
                    else -> state.copy(cars = (state.cars - 1)) + outStreet()
                }
            }
        }
    }

    private fun outStreet(): SideEffect<StreetEvent> = SideEffect {
        delay(delay)
        trafficLight?.addCar()
        null
    }

    val state: Stream<StreetState> = knot

    fun setTrafficLight(trafficLight: TrafficLight) {
        this.trafficLight = trafficLight
    }

    fun carIn() {
        knot.emit(StreetEvent.Plus)
    }

    fun carOut() {
        knot.emit(StreetEvent.Minus)
    }
}