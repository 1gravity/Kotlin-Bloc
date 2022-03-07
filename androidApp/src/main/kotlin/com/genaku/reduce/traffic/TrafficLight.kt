package com.genaku.reduce.traffic

import com.onegravity.knot.*
import com.onegravity.knot.state.SimpleKnotState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

sealed class TrafficState {
    object On : TrafficState()
    object Off : TrafficState()

    override fun toString(): String = when (this) {
        On -> "green"
        Off -> "red"
    }
}

sealed class TrafficEvent {
    object On : TrafficEvent()
    object Off : TrafficEvent()
    object Plus : TrafficEvent()
    object Minus : TrafficEvent()
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

    private val knot = knot<TrafficState, TrafficEvent, TrafficState> {
        knotState = SimpleKnotState(TrafficState.Off)
        dispatcherReduce = Dispatchers.IO
        dispatcherSideEffect = Dispatchers.IO

        reduce { state, event ->
            when (event) {
                TrafficEvent.Minus -> {
                    if (cars > 0) {
                        cars--
                        state + startMovement()
                    } else {
                        state.toEffect()
                    }
                }
                TrafficEvent.Plus -> {
                    cars++
                    state + startMovement()
                }
                TrafficEvent.Off -> TrafficState.Off + close()
                TrafficEvent.On -> {
                    val test = TrafficState.On + open() + startMovement()
                    test
                }
            }
        }
    }

    val state: Stream<TrafficState> = knot

    fun start() {
        knot.start(coroutineScope)
    }

    fun addCar() {
        cars++
        if (cars > limit) {
            coroutineScope.launch {
                knot.emit(TrafficEvent.On)
                delay(lightTime)
                knot.emit(TrafficEvent.Off)
            }
        }
    }

    fun setStreetIn(street: Street) {
        this.streetIn = street
    }

    fun setStreetOut(street: Street) {
        this.streetOut = street
    }

    private fun open() = SideEffect<TrafficEvent> {
        open.set(true)
        null
    }

    private fun close() = SideEffect<TrafficEvent> {
        open.set(false)
        null
    }

    private suspend fun startMovement() = SideEffect<TrafficEvent> {
        if (moving.get()) return@SideEffect null
        moving.set(true)
        while (open.get() && cars > 0) {
            delay(delay)
            streetIn?.carOut()
            streetOut?.carIn()
            cars--
        }
        moving.set(false)
        null
    }
}