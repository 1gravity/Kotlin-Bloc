package com.genaku.reduce.traffic

import com.genaku.reduce.*
import kotlinx.coroutines.CoroutineScope
import org.mym.plog.PLog

sealed class StreetState : State {
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

sealed class StreetIntent : StateIntent {
    object Plus : StreetIntent()
    object Minus : StreetIntent()
}

class Street(private val delay: Long) {

    private var trafficLight: TrafficLight? = null

    private val knot = easyKnot<StreetState, StreetIntent> {
        initialState = StreetState.Empty

        reduce { intent ->
            PLog.d("intent $this ${intent.javaClass.simpleName}")
            when (this) {
                StreetState.Empty -> when (intent) {
                    StreetIntent.Minus -> stateOnly
                    StreetIntent.Plus -> StreetState.Traffic(1) + outStreet()
                }
                is StreetState.Traffic -> when (intent) {
                    StreetIntent.Minus -> (this - 1).stateOnly
                    StreetIntent.Plus -> this + 1 + outStreet()
                }
            }
        }
    }

    private fun outStreet(): SideEffect<StreetIntent> = SideEffect {
        Thread.sleep(delay)
        trafficLight?.addCar()
        null
    }

    val state
        get() = knot.state

    fun start(coroutineScope: CoroutineScope) {
        knot.start(coroutineScope)
    }

    fun setTrafficLight(trafficLight: TrafficLight) {
        this.trafficLight = trafficLight
    }

    fun carIn() {
        knot.offerIntent(StreetIntent.Plus)
    }

    fun carOut() {
        knot.offerIntent(StreetIntent.Minus)
    }
}