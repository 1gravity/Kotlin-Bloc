package com.onegravity.bloc.sample.traffic

import com.onegravity.bloc.Stream
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.utils.logger
import kotlinx.coroutines.delay

data class TrafficState(val on: Boolean, val cars: Int) {
    override fun toString() = if (on) "green ($cars)" else "red ($cars)"
}

sealed class TrafficAction {
    object On : TrafficAction()
    object Off : TrafficAction()
    object Plus : TrafficAction()
    object Minus : TrafficAction()
}

class TrafficLight(
    context: BlocContext,
    private val delay: Long = 10,
    private val limit: Int = 20,
    private val lightTime: Long = 5000
) {

    private var streetIn: Street? = null
    private var streetOut: Street? = null

    private val commonState = blocState(TrafficState(false, 0))

    private val bloc = bloc<TrafficState, TrafficAction>(context, commonState) {
        thunkMatching<TrafficAction.Plus> { state, _, dispatch ->
            logger.w("Thunk 1: TrafficAction.Plus, state = $state")
            dispatch(TrafficAction.Plus)

            if (commonState.value.cars > limit) {
                dispatch(TrafficAction.On)
                delay(lightTime)
                dispatch(TrafficAction.Off)
            }
        }

        thunkMatching<TrafficAction.Minus> { state, _, dispatch ->
            logger.w("Thunk 2: TrafficAction.Minus, state = $state")
            dispatch(TrafficAction.Minus)

            while (commonState.value.on && commonState.value.cars > 0) {
                streetIn?.carOut()
                streetOut?.carIn()
                dispatch(TrafficAction.Minus)
            }
        }

        thunkMatching<TrafficAction.On> { state, _, dispatch ->
            logger.w("Thunk 3: TrafficAction.On, state = $state")
            dispatch(TrafficAction.On)

            while (commonState.value.on && commonState.value.cars > 0) {
                streetIn?.carOut()
                streetOut?.carIn()
                dispatch(TrafficAction.Minus)
            }
        }

        thunk { state, action, dispatch ->
            logger.w("Thunk 4: $action, state = $state")
//            dispatch(action)
        }

        reduce { state, action ->
            logger.w("Reduce: $action, state = $state")
            when (action) {
                TrafficAction.Plus -> state.copy(cars = state.cars + 1)
                TrafficAction.Minus -> state.copy(cars = (state.cars - 1).coerceAtLeast(0))
                TrafficAction.On -> state.copy(on = true)
                TrafficAction.Off -> state.copy(on = false)
            }
        }
    }

    val state: Stream<TrafficState> = bloc

    fun addCar() {
        bloc.emit(TrafficAction.Plus)
    }

    fun setStreetIn(street: Street) {
        this.streetIn = street
    }

    fun setStreetOut(street: Street) {
        this.streetOut = street
    }

}