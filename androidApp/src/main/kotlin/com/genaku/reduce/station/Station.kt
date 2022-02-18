package com.genaku.reduce.station

import com.onegravity.knot.*

sealed class StationState : State {
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

sealed class TrainIntent : StateIntent {
    class Arrive(val name: String) : TrainIntent()
    class Leave(val name: String) : TrainIntent()
}

sealed class BusIntent : StateIntent {
    class Arrive(val name: String) : BusIntent()
    class Leave(val name: String) : BusIntent()
}

sealed class LorryIntent : StateIntent {
    class Arrive(val name: String) : LorryIntent()
    class Leave(val name: String) : LorryIntent()
}