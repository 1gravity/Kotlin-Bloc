package com.onegravity.bloc.builder

import com.onegravity.bloc.BlocImpl
import com.onegravity.bloc.Reducer
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.BlocState
import com.onegravity.bloc.Thunk
import com.onegravity.bloc.fsm.GraphBuilder
import com.onegravity.bloc.fsm.Matcher
import com.onegravity.bloc.fsm.StateMachine
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class BlocBuilderImpl<State, Action, Proposal> {

    private var _reducer: Reducer<State, Action, Proposal>? = null
    private var _thunks: MutableList<Thunk<State, Action>> = ArrayList()
    private var _dispatcherThunks: CoroutineContext = Dispatchers.Default

    fun build(context: BlocContext, blocState: BlocState<State, Proposal>) = BlocImpl(
        context = context,
        blocState = blocState,
        reducer = checkNotNull(_reducer) { "reduce { } must be declared" },
        thunks = _thunks,
        dispatcherThunks = _dispatcherThunks,
    )

    /** A section for [Action] related declarations. */
    fun thunk(thunk: Thunk<State, Action>) {
        _thunks.add(thunk)
    }

    fun reduce(reducer: Reducer<State, Action, Proposal>) {
        _reducer = reducer
        stateMachine.transition(Event.OnMelted)
    }

//    inline fun <reified E : Event> event(noinline init: GraphBuilder.StateDefinitionBuilder<S>.() -> Unit) {
//        state(Matcher.any(), init)
//    }

    var dispatcherThunks: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _dispatcherThunks = value
        }

}

sealed class State {
    object Solid : State()
    object Liquid : State()
    object Gas : State()
}

sealed class Event {
    object OnMelted : Event()
    object OnFroze : Event()
    object OnVaporized : Event()
    object OnCondensed : Event()
}

sealed class SideEffect {
    object LogMelted : SideEffect()
    object LogFrozen : SideEffect()
    object LogVaporized : SideEffect()
    object LogCondensed : SideEffect()
}

val stateMachine = StateMachine.create<State, Event, SideEffect> {
    initialState(State.Solid)
    state<State.Solid> {
        on<Event.OnMelted> {
            transitionTo(State.Liquid, SideEffect.LogMelted)
        }
    }
    state<State.Liquid> {
        on<Event.OnFroze> {
            transitionTo(State.Solid, SideEffect.LogFrozen)
        }
        on<Event.OnVaporized> {
            transitionTo(State.Gas, SideEffect.LogVaporized)
        }
    }
    state<State.Gas> {
        on<Event.OnCondensed> {
            transitionTo(State.Liquid, SideEffect.LogCondensed)
        }
    }
}
