package com.genaku.reduce.station

sealed class StationState {
    object Empty : StationState()
    class Train(val name: String) : StationState()
    class Bus(val name: String) : StationState()
    class Lorry(val name: String) : StationState()

    fun getValue() = when (this) {
        Empty -> ""
        is Bus -> this.name
        is Lorry -> this.name
        is Train -> this.name
    }
}

sealed class TrainEvent {
    class Arrive(val name: String) : TrainEvent()
    class Leave(val name: String) : TrainEvent()
}

sealed class BusEvent {
    class Arrive(val name: String) : BusEvent()
    class Leave(val name: String) : BusEvent()
}

sealed class LorryEvent {
    class Arrive(val name: String) : LorryEvent()
    class Leave(val name: String) : LorryEvent()
}