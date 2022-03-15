package com.onegravity.knot.sample.traffic

import com.onegravity.bloc.Stream
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.logger
import com.onegravity.knot.state.knotState
import kotlinx.coroutines.delay

data class TrafficState(val on: Boolean, val cars: Int) {
    override fun toString() = if (on) "green ($cars)" else "red ($cars)"
}

sealed class TrafficEvent {
    object On : TrafficEvent()
    object Off : TrafficEvent()
    object Plus : TrafficEvent()
    object Minus : TrafficEvent()
}

class TrafficLight(
    context: BlocContext,
    private val delay: Long = 10,
    private val limit: Int = 20,
    private val lightTime: Long = 5000
) {

    private var streetIn: Street? = null
    private var streetOut: Street? = null

    private val commonState = knotState(TrafficState(false, 0))

    private val bloc = bloc<TrafficState, TrafficEvent>(context, commonState) {
        thunkMatching<TrafficEvent.Plus> { state, action, dispatch ->
            logger.w("Thunk 1: TrafficEvent.Plus, state = $state")
            dispatch(TrafficEvent.Plus)

            if (commonState.value.cars > limit) {
                dispatch(TrafficEvent.On)
                delay(lightTime)
                dispatch(TrafficEvent.Off)
            }
        }

        thunkMatching<TrafficEvent.Minus> { state, action, dispatch ->
            logger.w("Thunk 2: TrafficEvent.Minus, state = $state")
            dispatch(TrafficEvent.Minus)

            while (commonState.value.on && commonState.value.cars > 0) {
                streetIn?.carOut()
                streetOut?.carIn()
                dispatch(TrafficEvent.Minus)
            }
        }

        thunkMatching<TrafficEvent.On> { state, action , dispatch ->
            logger.w("Thunk 3: TrafficEvent.On, state = $state")
            dispatch(TrafficEvent.On)

            while (commonState.value.on && commonState.value.cars > 0) {
                streetIn?.carOut()
                streetOut?.carIn()
                dispatch(TrafficEvent.Minus)
            }
        }

        thunk { state, action, dispatch ->
            logger.w("Thunk 4: $action, state = $state")
//            dispatch(action)
        }

        reduce { state, action ->
            logger.w("Reduce: $action, state = $state")
            when (action) {
                TrafficEvent.Plus -> state.copy(cars = state.cars + 1)
                TrafficEvent.Minus -> state.copy(cars = (state.cars - 1).coerceAtLeast(0))
                TrafficEvent.On -> state.copy(on = true)
                TrafficEvent.Off -> state.copy(on = false)
            }
        }
    }

    val state: Stream<TrafficState> = bloc

    fun addCar() {
        bloc.emit(TrafficEvent.Plus)
    }

    fun setStreetIn(street: Street) {
        this.streetIn = street
    }

    fun setStreetOut(street: Street) {
        this.streetOut = street
    }

}