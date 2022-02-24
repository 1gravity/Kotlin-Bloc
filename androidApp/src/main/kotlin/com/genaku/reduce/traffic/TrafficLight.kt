package com.genaku.reduce.traffic

import com.onegravity.knot.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.mym.plog.PLog
import java.util.concurrent.atomic.AtomicBoolean

sealed class TrafficState : State {
    object On : TrafficState()
    object Off : TrafficState()

    override fun toString(): String = when (this) {
        On -> "green"
        Off -> "red"
    }
}

sealed class TrafficIntent : StateIntent {
    object On : TrafficIntent()
    object Off : TrafficIntent()
    object Plus : TrafficIntent()
    object Minus : TrafficIntent()
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

    private val knot = suspendKnot<TrafficState, TrafficIntent> {
        initialState = TrafficState.Off

        dispatcher(Dispatchers.IO)

        reduce { state, intent ->
            PLog.d("intent $intent for state $this")
            when (intent) {
                TrafficIntent.Minus -> {
                    if (cars > 0) {
                        cars--
                        state + startMovement()
                    } else {
                        state.toEffect
                    }
                }
                TrafficIntent.Plus -> {
                    cars++
                    state + startMovement()
                }
                TrafficIntent.Off -> TrafficState.Off + close()
                TrafficIntent.On -> TrafficState.On + open() + startMovement()
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

    private fun open() = SuspendSideEffect<TrafficIntent> {
        PLog.d("open")
        open.set(true)
        null
    }

    private fun close() = SuspendSideEffect<TrafficIntent> {
        PLog.d("close")
        open.set(false)
        null
    }

    private suspend fun startMovement() = SuspendSideEffect<TrafficIntent> {
        PLog.d("startMovement")
        if (moving.get()) return@SuspendSideEffect null
        PLog.d("start moving ${moving.get()} ${open.get()}")
        moving.set(true)
        while (open.get() && cars > 0) {
            delay(delay)
            PLog.d("move car $cars")
            streetIn?.carOut()
            streetOut?.carIn()
            cars--
        }
        moving.set(false)
        PLog.d("end moving ${moving.get()}")
        null
    }
}