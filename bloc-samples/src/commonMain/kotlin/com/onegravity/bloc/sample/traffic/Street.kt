package com.onegravity.bloc.sample.traffic

import com.onegravity.bloc.Stream
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import kotlinx.coroutines.delay

data class StreetState(val cars: Int) {
    operator fun plus(cars: Int): StreetState = StreetState(this.cars + cars)
    operator fun minus(cars: Int): StreetState = StreetState(this.cars - cars)
    override fun toString() = cars.toString()
}

sealed class StreetAction {
    object Plus : StreetAction()
    object Minus : StreetAction()
}

class Street(context: BlocContext, private val delay: Long) {

    private var trafficLight: TrafficLight? = null

    private val bloc = bloc<StreetState, StreetAction>(context, StreetState(0)) {
        thunkMatching<StreetAction.Plus> { _, _, dispatch ->
            dispatch(StreetAction.Plus)
            delay(delay)
            trafficLight?.addCar()
        }

        reduce { state, action ->
            val cars = state.cars
            when (action) {
                StreetAction.Plus -> StreetState(cars + 1)
                StreetAction.Minus -> StreetState((cars - 1).coerceAtLeast(0))
            }
        }
    }

    val state: Stream<StreetState> = bloc

    fun setTrafficLight(trafficLight: TrafficLight) {
        this.trafficLight = trafficLight
    }

    fun carIn() {
        bloc.emit(StreetAction.Plus)
    }

    fun carOut() {
        bloc.emit(StreetAction.Minus)
    }
}