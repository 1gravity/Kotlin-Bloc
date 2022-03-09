package com.genaku.reduce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onegravity.knot.Stream
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.knot
import com.onegravity.knot.knotState
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch

class MainViewModel(context: KnotContext) : ViewModel() {

    private val commonState = knotState(initialState = SampleState.FIRST)

    private val knot = knot<SampleState, SampleEvent, SampleState, SampleSideEffect>(context) {

        knotState = commonState

        reduce { state, event ->
            when (event) {
                SampleEvent.ONE -> state.toEffect()
                SampleEvent.TWO -> SampleState.SECOND + SampleSideEffect.YES + SampleSideEffect.NO
                SampleEvent.THREE -> state.toEffect()
            }
        }

        execute { action ->
            delay(200)
            when (action) {
                SampleSideEffect.YES -> SampleEvent.THREE
                SampleSideEffect.NO -> null
            }
        }
    }

    val state: Stream<SampleState> = commonState

    fun d() {
        viewModelScope.launch {
            repeat(7) {
                knot.emit(SampleEvent.ONE)
                knot.emit(SampleEvent.TWO)
            }
        }
    }
}

enum class SampleState {
    FIRST,
    SECOND,
    THIRD
}

enum class SampleEvent {
    ONE,
    TWO,
    THREE
}

enum class SampleSideEffect {
    YES,
    NO
}