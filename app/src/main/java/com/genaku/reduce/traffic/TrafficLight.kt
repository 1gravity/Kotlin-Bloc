package com.genaku.reduce.traffic

import android.util.Log
import com.genaku.reduce.Action
import com.genaku.reduce.Intent
import com.genaku.reduce.State
import com.genaku.reduce.knot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

sealed class TrafficState : State {
    object On : TrafficState()
    object Off : TrafficState()

    fun getValue(): String {
        return when (this) {
            On -> "green"
            Off -> "red"
        }
    }
}

sealed class TrafficIntent : Intent {
    object On : TrafficIntent()
    object Off : TrafficIntent()
    object Plus : TrafficIntent()
    object Minus : TrafficIntent()
}

sealed class TrafficAction : Action {
    object Open : TrafficAction()
    object Close : TrafficAction()
    object Changed : TrafficAction()
}

class TrafficLight(
    private val coroutineScope: CoroutineScope,
    private val delay: Long = 10,
    private val limit: Int = 20,
    private val lightTime: Long = 5000
) {

    private var streetIn: Street? = null
    private var streetOut: Street? = null

    private var cars = 0

    private var open = AtomicBoolean(false)
    private var moving = AtomicBoolean(false)

    private val knot = knot<TrafficState, TrafficIntent, TrafficAction> {
        initialState = TrafficState.Off

        intents { intent ->
            when (intent) {
                TrafficIntent.Minus -> {
                    if (cars > 0) {
                        cars--
                        this + TrafficAction.Changed
                    } else {
                        this.stateOnly
                    }
                }
                TrafficIntent.Plus -> {
                    cars++
                    this + TrafficAction.Changed
                }
                TrafficIntent.Off -> TrafficState.Off + TrafficAction.Close
                TrafficIntent.On -> TrafficState.On + TrafficAction.Open + TrafficAction.Changed
            }
        }

        suspendActions { action ->
            when (action) {
                TrafficAction.Changed -> {
                    startMovement()
                    null
                }
                TrafficAction.Close -> {
                    Log.d("TRAFLOG:" + this@TrafficLight.hashCode(), "close")
                    open.set(false)
                    null
                }
                TrafficAction.Open -> {
                    Log.d("TRAFLOG:" + this@TrafficLight.hashCode(), "open")
                    open.set(true)
                    null
                }
            }
        }
    }

    val state
        get() = knot.state

    fun start() {
        knot.start(coroutineScope)
    }

    fun addCar() {
        cars++
        if (cars > limit) {
            coroutineScope.launch {
                knot.offerIntent(TrafficIntent.On)
                delay(lightTime)
                knot.offerIntent(TrafficIntent.Off)
            }
        }
    }

    fun setStreetIn(street: Street) {
        this.streetIn = street
    }

    fun setStreetOut(street: Street) {
        this.streetOut = street
    }

    private fun startMovement() {
        Log.d("TRAFLOG:" + this@TrafficLight.hashCode(), "startMovement")
        if (moving.get()) return
        Log.d("TRAFLOG:" + this@TrafficLight.hashCode(), "start moving ${moving.get()} ${open.get()}")
        coroutineScope.launch {
            moving.set(true)
            while (open.get() && cars > 0) {
                delay(delay)
                Log.d("TRAFLOG:" + this@TrafficLight.hashCode(), "move car $cars")
                streetIn?.carOut()
                streetOut?.carIn()
                cars--
            }
            moving.set(false)
        }
        Log.d("TRAFLOG:" + this@TrafficLight.hashCode(), "end moving ${moving.get()}")
    }
}