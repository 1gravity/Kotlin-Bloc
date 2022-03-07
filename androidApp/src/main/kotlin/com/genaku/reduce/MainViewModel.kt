package com.genaku.reduce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.onegravity.knot.*
import com.onegravity.knot.state.SimpleKnotState

class MainViewModel : ViewModel() {

    private val commonState = SimpleKnotState(SampleState.FIRST)

    private val knot = knot<SampleState, SampleEvent, SampleState, SampleSideEffect> {

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

    init {
        knot.start(viewModelScope)
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

enum class SampleEvent {
    ONE,
    TWO,
    THREE
}

enum class SampleSideEffect {
    YES,
    NO
}