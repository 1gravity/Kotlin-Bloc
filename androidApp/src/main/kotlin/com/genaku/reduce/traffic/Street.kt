package com.genaku.reduce.traffic

import com.onegravity.knot.*
import kotlinx.coroutines.CoroutineScope

sealed class StreetState {
    object Empty : StreetState()
    class Traffic(val cars: Int) : StreetState()

    operator fun plus(cars: Int): StreetState = when (this) {
        Empty -> Traffic(cars)
        is Traffic -> Traffic(this.cars + cars)
    }

    operator fun minus(cars: Int): StreetState = when (this) {
        Empty -> this
        is Traffic -> {
            val newCars = this.cars - cars
            if (newCars > 0) Traffic(newCars) else Empty
        }
    }

    val value: Int
        get() = when (this) {
            Empty -> 0
            is Traffic -> this.cars
        }

    override fun toString(): String = value.toString()
}

sealed class StreetEvent {
    object Plus : StreetEvent()
    object Minus : StreetEvent()
}

class Street(private val delay: Long) {

    private var trafficLight: TrafficLight? = null

    private val knot = simpleKnot<StreetState, StreetEvent> {
        initialState = StreetState.Empty

        reduce { state, event ->
            when (state) {
                StreetState.Empty -> when (event) {
                    StreetEvent.Minus -> state.toEffect()
                    StreetEvent.Plus -> StreetState.Traffic(1) + outStreet()
                }
                is StreetState.Traffic -> when (event) {
                    StreetEvent.Minus -> (state - 1).toEffect()
                    StreetEvent.Plus -> state + 1 + outStreet()
                }
            }
        }
    }

    private fun outStreet(): SideEffect<StreetEvent> = SideEffect {
        Thread.sleep(delay)
        trafficLight?.addCar()
        null
    }

    val state: Stream<StreetState> = knot

    fun start(coroutineScope: CoroutineScope) {
        knot.start(coroutineScope)
    }

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