package com.onegravity.bloc

import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

class BlocBuilder<State, Action: Any, SE, Proposal> {

    private val _thunks = ArrayList<MatcherThunk<State, Action>>()
    private val _reducers = ArrayList<MatcherReducer<State, Action, Effect<Proposal, SE>>>()
    private var _dispatcher: CoroutineContext = Dispatchers.Default

    fun build(context: BlocContext, blocState: BlocState<State, Proposal>) = BlocImpl(
        blocContext = context,
        blocState = blocState,
        thunks = _thunks,
        reducers = _reducers,
        dispatcher = _dispatcher,
    )

    /* *** Thunks *** */

    @BlocDSL
    fun thunk(thunk: Thunk<State, Action>) {
        _thunks.add(MatcherThunk(null, thunk))
    }

    @BlocDSL
    @JvmName("thunkMatching")
    inline fun <reified A : Action> thunk(noinline thunk: Thunk<State, Action>) {
        addThunk(Matcher.any<Action, A>(), thunk)
    }

    @BlocInternal
    fun <A : Action> addThunk(matcher: Matcher<Action, A>, thunk: Thunk<State, Action>) {
        _thunks.add(MatcherThunk(matcher, thunk))
    }

    /* Reducer with state but without side effect(s) */

    @BlocDSL
    fun state(reducer: Reducer<State, Action, Proposal>) {
        val reducerNoSideEffect: Reducer<State, Action, Effect<Proposal, SE>> = {
            reducer.invoke(this).noSideEffect
        }
        _reducers.add(MatcherReducer(null, reducerNoSideEffect, true))
    }

    @BlocDSL
    @JvmName("reduceMatching")
    inline fun <reified A : Action> state(noinline reducer: Reducer<State, Action, Proposal>) {
        val reducerNoSideEffect: Reducer<State, Action, Effect<Proposal, SE>> = {
            reducer.invoke(this).noSideEffect
        }
        addReducer(Matcher.any<Action, A>(), reducerNoSideEffect, true)
    }

    /* Reducer without state but with side effect(s) */

    @BlocDSL
    fun sideEffect(sideEffect: SideEffect<State, Action, SE>) {
        val reducerNoState: Reducer<State, Action, Effect<Proposal, SE>> = {
            Effect(null, sideEffect.invoke(this))
        }
        _reducers.add(MatcherReducer(null, reducerNoState, false))
    }

    @BlocDSL
    @JvmName("sideEffectMatching")
    inline fun <reified A : Action> sideEffect(noinline sideEffect: SideEffect<State, Action, SE>) {
        val reducerNoState: Reducer<State, Action, Effect<Proposal, SE>> = {
            Effect(null, sideEffect.invoke(this))
        }
        addReducer(Matcher.any<Action, A>(), reducerNoState, false)
    }

    /* Reducers with state and side effect(s) */

    @BlocDSL
    fun reduce(reducer: Reducer<State, Action, Effect<Proposal, SE>>) {
        _reducers.add(MatcherReducer(null, reducer, true))
    }

    @BlocDSL
    @JvmName("reduceWithSideEffectMatching")
    inline fun <reified A : Action> reduce(
        noinline reducer: Reducer<State, Action, Effect<Proposal, SE>>
    ) {
        addReducer(Matcher.any<Action, A>(), reducer, true)
    }

    @BlocInternal
    fun <A : Action> addReducer(
        matcher: Matcher<Action, A>,
        reducer: Reducer<State, Action, Effect<Proposal, SE>>,
        expectsProposal: Boolean
    ) {
        _reducers
            .firstOrNull { it.matcher != null && it.matcher == matcher }
            ?.run { logger.e("Duplicate reduce<${matcher.clazzName()}>") }
        _reducers.add(MatcherReducer(matcher, reducer, expectsProposal))
    }

    /* Dispatcher */

    @BlocDSL
    var dispatcher: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _dispatcher = value
        }

    /* Extension functions */

    @BlocDSL
    val Proposal.noSideEffect
        get() = Effect<Proposal, SE>(this, emptyList())

    @BlocDSL
    @JvmName("proposalAnd")
    infix fun Proposal.and(sideEffect: SE) = Effect(this, sideEffect)

    @BlocDSL
    @JvmName("sideEffectAnd")
    infix fun SE.and(proposal: Proposal) = Effect(proposal, this)

    @BlocDSL
    @JvmName("sideEffectAndSideEffect")
    infix fun SE.and(sideEffect: SE): List<SE> = listOf(this, sideEffect)

}
