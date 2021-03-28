package com.genaku.reduce.traffic

import android.util.Log
import com.genaku.reduce.Action
import com.genaku.reduce.Intent
import com.genaku.reduce.State
import com.genaku.reduce.knot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

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

    fun getValue(): Int {
        return when (this) {
            Empty -> 0
            is Traffic -> this.cars
        }
    }
}

sealed class StreetAction : Action {
    object Out : StreetAction()
}

sealed class StreetIntent : Intent {
    object Plus : StreetIntent()
    object Minus : StreetIntent()
}

class Street(private val delay: Long) {

    private var trafficLight: TrafficLight? = null

    private val knot = knot<StreetState, StreetIntent, StreetAction> {
        initialState = StreetState.Empty

        intents { intent ->
            Log.d("TRAFLOG:" + this@Street.hashCode(), "intent ${this.getValue()} ${intent.javaClass.simpleName}")
            when (this) {
                StreetState.Empty -> when (intent) {
                    StreetIntent.Minus -> this.stateOnly
                    StreetIntent.Plus -> StreetState.Traffic(1) + StreetAction.Out
                }
                is StreetState.Traffic -> when (intent) {
                    StreetIntent.Minus -> (this - 1).stateOnly
                    StreetIntent.Plus -> this + 1 + StreetAction.Out
                }
            }
        }

        suspendActions { action ->
            when (action) {
                StreetAction.Out -> {
                    delay(delay)
                    trafficLight?.addCar()
                    null
                }
            }
        }
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