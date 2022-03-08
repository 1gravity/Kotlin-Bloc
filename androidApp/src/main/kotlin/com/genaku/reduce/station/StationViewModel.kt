package com.genaku.reduce.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onegravity.knot.SideEffect
import com.onegravity.knot.Stream
import com.onegravity.knot.knot
import com.onegravity.knot.knotState
import kotlinx.coroutines.delay

class StationViewModel : ViewModel() {

    private val stationState = knotState<StationState>(StationState.Empty)

    private val arrive = " arrive"
    private val departure = " departure"

    private class Vehicle(val name: String, val delay: Long, var num: Int = 0) {
        override fun toString(): String = "$name $num"
    }

    private suspend fun <Event> arrive(vehicle: Vehicle, create: (Vehicle) -> Event) = SideEffect {
        delay(vehicle.delay)
        vehicle.num++
        create(vehicle)
    }

    private suspend fun <Event> leave(vehicle: Vehicle, create: (Vehicle) -> Event) = SideEffect {
        delay(vehicle.delay)
        create(vehicle)
    }

    private val busKnot = knot<StationState, BusEvent, StationState, SideEffect<BusEvent>> {
        knotState = stationState

        val bus = Vehicle("Bus", 800)

        reduce { _, event ->
            when (event) {
                is BusEvent.Arrive -> StationState.Bus(event.name + arrive) +
                        leave(bus) { BusEvent.Leave("$it") }
                is BusEvent.Leave -> StationState.Bus(event.name + departure) +
                        arrive(bus) { BusEvent.Arrive("$it") }
            }
        }
    }

    private val trainKnot = knot<StationState, TrainEvent, StationState, SideEffect<TrainEvent>> {
        knotState = stationState

        val train = Vehicle("Train", 600)

        reduce { _, event ->
            when (event) {
                is TrainEvent.Arrive -> StationState.Train(event.name + arrive) +
                        leave(train) { TrainEvent.Leave("$it") }
                is TrainEvent.Leave -> StationState.Train(event.name + departure) +
                        arrive(train) { TrainEvent.Arrive("$it") }
            }
        }
    }

    private val lorryKnot = knot<StationState, LorryEvent, StationState, SideEffect<LorryEvent>> {
        knotState = stationState
        val lorry = Vehicle("Lorry", 250)

        reduce { _, event ->
            when (event) {
                is LorryEvent.Arrive -> StationState.Lorry(event.name + arrive) +
                        leave(lorry) { LorryEvent.Leave("$it") }
                is LorryEvent.Leave -> StationState.Lorry(event.name + departure) +
                        arrive(lorry) { LorryEvent.Arrive("$it") }
            }
        }
    }

    private var case = 0

    val state: Stream<StationState> = stationState

    fun switch() {
        when (case) {
            0 -> {
                lorryKnot.stop()
                trainKnot.stop()
                busKnot.start(viewModelScope)
                busKnot.emit(BusEvent.Arrive("start"))
            }
            1 -> {
                busKnot.stop()
                trainKnot.stop()
                lorryKnot.start(viewModelScope)
                lorryKnot.emit(LorryEvent.Arrive("start"))
            }
            2 -> {
                lorryKnot.stop()
                busKnot.stop()
                trainKnot.start(viewModelScope)
                trainKnot.emit(TrainEvent.Arrive("start"))
            }
            else -> {
                case = -1
                stop()
            }
        }
        case++
    }

    private fun stop() {
        busKnot.stop()
        lorryKnot.stop()
        trainKnot.stop()
    }

    override fun onCleared() {
        stop()
        super.onCleared()
    }
}