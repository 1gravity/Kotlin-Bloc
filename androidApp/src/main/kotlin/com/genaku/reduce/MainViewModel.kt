package com.genaku.reduce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.onegravity.knot.*

class MainViewModel : ViewModel() {

    private val commonState = CoroutineKnotState(SampleState.FIRST)

    private val knot = knot<SampleState, SampleIntent, SampleAction> {

        knotState = commonState

        reduce { state, intent ->
            when (intent) {
                SampleIntent.ONE -> state.toEffect
                SampleIntent.TWO -> SampleState.SECOND + SampleAction.YES + SampleAction.NO
                SampleIntent.THREE -> state.toEffect
            }
        }

        suspendActions { action ->
            delay(200)
            when (action) {
                SampleAction.YES -> SampleIntent.THREE
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
                knot.offerIntent(SampleIntent.ONE)
                knot.offerIntent(SampleIntent.TWO)
            }
        }
    }

    override fun onCleared() {
        knot.stop()
        super.onCleared()
    }
}

enum class SampleState : State {
    FIRST,
    SECOND,
    THIRD
}

enum class SampleIntent : StateIntent {
    ONE,
    TWO,
    THREE
}

enum class SampleAction : StateAction {
    YES,
    NO
}