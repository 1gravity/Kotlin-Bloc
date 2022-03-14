package com.onegravity.knot.sample.traffic

import com.onegravity.bloc.Stream
import com.onegravity.knot.*
import com.onegravity.bloc.context.BlocContext
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

class Street(context: BlocContext, private val delay: Long) {

    private var trafficLight: TrafficLight? = null

    private val knot = knot<StreetState, StreetEvent>(context, StreetState(0)) {
        reduce { state, event ->
            val cars = state.cars
            when (event) {
                StreetEvent.Plus -> state.copy(cars = cars + 1) + outStreet()
                StreetEvent.Minus -> state.copy(cars = (cars - 1).coerceAtLeast(0)).toEffect()
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