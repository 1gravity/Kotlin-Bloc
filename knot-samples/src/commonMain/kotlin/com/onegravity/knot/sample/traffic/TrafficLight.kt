package com.onegravity.knot.sample.traffic

import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

data class TrafficState(val on: Boolean, val cars: Int) {
    override fun toString() = if (on) "green" else "red"
}

sealed class TrafficEvent {
    object On : TrafficEvent()
    object Off : TrafficEvent()
    object Plus : TrafficEvent()
    object Minus : TrafficEvent()
}

class TrafficLight(
    context: KnotContext,
    private val delay: Long = 10,
    private val limit: Int = 20,
    private val lightTime: Long = 5000
) {

    private var streetIn: Street? = null
    private var streetOut: Street? = null

    private val commonState = knotState(TrafficState(false, 0))

    private val knot = knot<TrafficState, TrafficEvent, TrafficState>(context) {
        knotState = commonState
        dispatcherReduce = Dispatchers.Default
        dispatcherSideEffect = Dispatchers.Default

        reduce { state, event ->
            when (event) {
                TrafficEvent.Minus -> {
                    if (state.cars > 0) {
                        state.copy(cars = state.cars - 1) + startMovement()
                    } else {
                        state.toEffect()
                    }
                }
                TrafficEvent.Plus -> state.copy(cars = state.cars + 1) + startMovement()
                TrafficEvent.Off -> state.copy(on = false).toEffect()
                TrafficEvent.On -> state.copy(on = true) + startMovement()
            }
        }
    }

    private fun startMovement() = SideEffect<TrafficEvent> {
        if (commonState.value.on && commonState.value.cars > 0) {
            delay(delay)
            streetIn?.carOut()
            streetOut?.carIn()
            TrafficEvent.Minus
        } else null
    }

    val state: Stream<TrafficState> = knot

    suspend fun addCar() {
        val cars = commonState.value.cars + 1
        commonState.emit(commonState.value.copy(cars = cars))
        if (cars > limit) {
            knot.emit(TrafficEvent.On)
            delay(lightTime)
            knot.emit(TrafficEvent.Off)
        }
    }

    fun setStreetIn(street: Street) {
        this.streetIn = street
    }

    fun setStreetOut(street: Street) {
        this.streetOut = street
    }

}