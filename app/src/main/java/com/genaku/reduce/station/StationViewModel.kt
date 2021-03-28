package com.genaku.reduce.station

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.genaku.reduce.CoroutineKnotState
import com.genaku.reduce.knot
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class StationViewModel : ViewModel() {

    private val stationState = CoroutineKnotState<StationState>(StationState.Empty)

    private val arrive = " arrive"
    private val departure = " departure"
    
    private val busKnot = knot<StationState, BusIntent, BusAction> {
        knotState = stationState

        intents { intent ->
            when (intent) {
                is BusIntent.Arrive -> StationState.Bus(intent.name +arrive) + BusAction.Leave
                is BusIntent.Leave -> StationState.Bus(intent.name + departure) + BusAction.Arrive
            }
        }

        var num = 0

        suspendActions { action ->
            when (action) {
                BusAction.Arrive -> {
                    delay(800)
                    num++
                    BusIntent.Arrive("Bus $num")
                }
                BusAction.Leave -> {
                    delay(800)
                    BusIntent.Leave("Bus $num")
                }
            }
        }
    }

    private val trainKnot = knot<StationState, TrainIntent, TrainAction> {
        knotState = stationState

        intents { intent ->
            when (intent) {
                is TrainIntent.Arrive -> StationState.Train(intent.name + arrive) + TrainAction.Leave
                is TrainIntent.Leave -> StationState.Train(intent.name + departure) + TrainAction.Arrive
            }
        }

        var num = 0

        suspendActions { action ->
            when (action) {
                TrainAction.Arrive -> {
                    delay(600)
                    num++
                    TrainIntent.Arrive("Train $num")
                }
                TrainAction.Leave -> {
                    delay(600)
                    TrainIntent.Leave("Train $num")
                }
            }
        }
    }

    private val lorryKnot = knot<StationState, LorryIntent, LorryAction> {
        knotState = stationState

        intents { intent ->
            when (intent) {
                is LorryIntent.Arrive -> StationState.Lorry(intent.name + arrive) + LorryAction.Leave
                is LorryIntent.Leave -> StationState.Lorry(intent.name + departure) + LorryAction.Arrive
            }
        }

        var num = 0

        suspendActions { action ->
            when (action) {
                LorryAction.Arrive -> {
                    delay(250)
                    num++
                    LorryIntent.Arrive("Lorry $num")
                }
                LorryAction.Leave -> {
                    delay(250)
                    LorryIntent.Leave("Lorry $num")
                }
            }
        }
    }

    private var case = 0

    val state: StateFlow<StationState>
        get() = stationState.state

    fun switch() {
        when (case) {
            0 -> {
                lorryKnot.stop()
                trainKnot.stop()
                busKnot.start(viewModelScope)
                busKnot.offerIntent(BusIntent.Arrive("start"))
            }
            1 -> {
                busKnot.stop()
                trainKnot.stop()
                lorryKnot.start(viewModelScope)
                lorryKnot.offerIntent(LorryIntent.Arrive("start"))
            }
            2 -> {
                lorryKnot.stop()
                busKnot.stop()
                trainKnot.start(viewModelScope)
                trainKnot.offerIntent(TrainIntent.Arrive("start"))
            }
            else -> {
                case = -1
                stop()
            }
        }
        case++
    }

    fun stop() {
        busKnot.stop()
        lorryKnot.stop()
        trainKnot.stop()
    }

    override fun onCleared() {
        stop()
        super.onCleared()
    }

}