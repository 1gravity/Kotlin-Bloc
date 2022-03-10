package com.genaku.reduce.station

import androidx.lifecycle.ViewModel
import com.onegravity.knot.SideEffect
import com.onegravity.knot.Stream
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.knot
import com.onegravity.knot.knotState
import com.onegravity.knot.sample.station.BusEvent
import com.onegravity.knot.sample.station.LorryEvent
import com.onegravity.knot.sample.station.StationState
import com.onegravity.knot.sample.station.TrainEvent
import kotlinx.coroutines.delay

class StationViewModel(context: KnotContext) : ViewModel() {

    private val stationState = knotState<StationState>(StationState.Empty)

    val state: Stream<StationState> = stationState

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

    private val busKnot = knot<StationState, BusEvent, StationState, SideEffect<BusEvent>>(context, stationState) {
        val bus = Vehicle("Bus", 800)

        reduce { _, event ->
            when (event) {
                is BusEvent.Arrive -> StationState.Bus(event.name + arrive) +
                        leave(bus) { BusEvent.Leave("$it") }
                is BusEvent.Leave -> StationState.Bus(event.name + departure) +
                        arrive(bus) { BusEvent.Arrive("$it") }
            }
        }

        execute { it.block.invoke() }
    }

    private val trainKnot = knot<StationState, TrainEvent, StationState, SideEffect<TrainEvent>>(context, stationState) {
        val train = Vehicle("Train", 600)

        reduce { _, event ->
            when (event) {
                is TrainEvent.Arrive -> StationState.Train(event.name + arrive) +
                        leave(train) { TrainEvent.Leave("$it") }
                is TrainEvent.Leave -> StationState.Train(event.name + departure) +
                        arrive(train) { TrainEvent.Arrive("$it") }
            }
        }

        execute { it.block.invoke() }
    }

    private val lorryKnot = knot<StationState, LorryEvent, StationState, SideEffect<LorryEvent>>(context, stationState) {
        val lorry = Vehicle("Lorry", 250)

        reduce { _, event ->
            when (event) {
                is LorryEvent.Arrive -> StationState.Lorry(event.name + arrive) +
                        leave(lorry) { LorryEvent.Leave("$it") }
                is LorryEvent.Leave -> StationState.Lorry(event.name + departure) +
                        arrive(lorry) { LorryEvent.Arrive("$it") }
            }
        }

        execute { it.block.invoke() }
    }

    private var case = 0

    fun switch() {
        when (case) {
            0 -> busKnot.emit(BusEvent.Arrive("start"))
            1 -> lorryKnot.emit(LorryEvent.Arrive("start"))
            2 -> trainKnot.emit(TrainEvent.Arrive("start"))
            else -> case = -1
        }
        case++
    }

}