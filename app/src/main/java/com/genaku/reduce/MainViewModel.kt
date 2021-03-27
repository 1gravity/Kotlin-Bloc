package com.genaku.reduce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val commonState = CoroutineKnotState(SampleState.FIRST)

    private val knot = coroutineKnot<SampleState, SampleChange, SampleAction> {

        knotState = commonState

        changes { change ->
            when (change) {
                SampleChange.ONE -> SampleState.FIRST.only
                SampleChange.TWO -> SampleState.SECOND + SampleAction.YES + SampleAction.NO
                SampleChange.THREE -> SampleState.THIRD.only
            }
        }

        suspendActions { action ->
            delay(200)
            when (action) {
                SampleAction.YES -> SampleChange.THREE
                SampleAction.NO -> null
            }
        }
    }

    init {
        knot.start(viewModelScope)
    }

    val state: StateFlow<SampleState>
        get() = commonState.state

    fun d() {
        viewModelScope.launch {
            repeat(7) {
                knot.offerChange(SampleChange.ONE)
                knot.offerChange(SampleChange.TWO)
            }
        }
    }

    override fun onCleared() {
        knot.stop()
        super.onCleared()
    }
}

enum class SampleState {
    FIRST,
    SECOND,
    THIRD
}

enum class SampleChange {
    ONE,
    TWO,
    THREE
}

enum class SampleAction {
    YES,
    NO
}