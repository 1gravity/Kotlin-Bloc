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

sealed class StreetEvent {
    object Plus : StreetEvent()
    object Minus : StreetEvent()
}

class Street(context: BlocContext, private val delay: Long) {

    private var trafficLight: TrafficLight? = null

    private val bloc = bloc<StreetState, StreetEvent>(context, StreetState(0)) {
        thunkMatching<StreetEvent.Plus> { _, action, dispatch ->
            dispatch(StreetEvent.Plus)
            delay(delay)
            trafficLight?.addCar()
        }

        reduce { state, event ->
            val cars = state.cars
            when (event) {
                StreetEvent.Plus -> StreetState(cars + 1)
                StreetEvent.Minus -> StreetState((cars - 1).coerceAtLeast(0))
            }
        }
    }

    val state: Stream<StreetState> = bloc

    fun setTrafficLight(trafficLight: TrafficLight) {
        this.trafficLight = trafficLight
    }

    fun carIn() {
        bloc.emit(StreetEvent.Plus)
    }

    fun carOut() {
        bloc.emit(StreetEvent.Minus)
    }
}