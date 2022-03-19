package com.onegravity.bloc

import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

// TODO consider combining side effects with reducers (analogous Knot)
class BlocBuilder<State, Action: Any, SE, Proposal> {

    private val _thunks = ArrayList<MatcherThunk<State, Action>>()
    private val _reducers = ArrayList<MatcherReducer<State, Action, Proposal>>()
    private val _reducersSideEffect = ArrayList<MatcherReducer<State, Action, Effect<Proposal, SE>>>()
    private val _sideEffects = ArrayList<MatcherSideEffect<State, Action, SE>>()
    private var _dispatcher: CoroutineContext = Dispatchers.Default

    fun build(context: BlocContext, blocState: BlocState<State, Proposal>) = BlocImpl(
        blocContext = context,
        blocState = blocState,
        thunks = _thunks,
        reducers = _reducers,
        sideEffects = _sideEffects,
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

    /* *** Reducers *** */

    @BlocDSL
    fun reduce(reducer: Reducer<State, Action, Proposal>) {
        _reducers.add(MatcherReducer(null, reducer))
    }

    @BlocDSL
    @JvmName("reducerMatching")
    inline fun <reified A : Action> reduce(noinline reducer: Reducer<State, Action, Proposal>) {
        addReducer(Matcher.any<Action, A>(), reducer)
        val newReducer: Reducer<State, Action, Effect<Proposal, SE>> = { reducer.invoke(this).noSideEffect }
        addReducer2(Matcher.any<Action, A>(), newReducer)
    }

    @BlocDSL
    @JvmName("reduceWithSideEffect")
    inline fun <reified A : Action> reduceWithSideEffect(noinline reducer: Reducer<State, Action, Effect<Proposal, SE>>) {
        addReducer2(Matcher.any<Action, A>(), reducer)
    }

    @BlocInternal
    fun <A : Action> addReducer2(
        matcher: Matcher<Action, A>,
        reducer: Reducer<State, Action, Effect<Proposal, SE>>
    ) {
        _reducers
            .firstOrNull { it.matcher != null && it.matcher == matcher }
            ?.run { logger.e("Duplicate reduce<${matcher.clazzName()}>") }
        _reducersSideEffect.add(MatcherReducer(matcher, reducer))
    }

    @BlocInternal
    fun <A : Action> addReducer(
        matcher: Matcher<Action, A>,
        reducer: Reducer<State, Action, Proposal>
    ) {
        _reducers
            .firstOrNull { it.matcher != null && it.matcher == matcher }
            ?.run { logger.e("Duplicate reduce<${matcher.clazzName()}>") }
        _reducers.add(MatcherReducer(matcher, reducer))
    }

    /* *** SideEffects *** */

    @BlocDSL
    fun sideEffect(sideEffect: SideEffect<State, Action, SE>) {
        _sideEffects.add(MatcherSideEffect(null, sideEffect))
    }

    @BlocDSL
    @JvmName("sideEffectMatching")
    inline fun <reified A : Action> sideEffect(noinline sideEffect: SideEffect<State, Action, SE>) {
        addSideEffect(Matcher.any<Action, A>(), sideEffect)
    }

    @BlocInternal
    fun <A : Action> addSideEffect(
        matcher: Matcher<Action, A>,
        sideEffect: SideEffect<State, Action, SE>
    ) {
        _sideEffects
            .firstOrNull { it.matcher != null && it.matcher == matcher }
            ?.run { logger.e("Duplicate sideEffect<${matcher.clazzName()}>") }
        _sideEffects.add(MatcherSideEffect(matcher, sideEffect))
    }

    /* *** Dispatcher *** */

    @BlocDSL
    var dispatcher: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _dispatcher = value
        }

    @BlocDSL
    val Proposal.noSideEffect: Effect<Proposal, SE> get() = Effect(this)

    @BlocDSL
    operator fun Proposal.plus(sideEffect: SE) = Effect(this, listOf(sideEffect))

    @BlocDSL
    infix fun Proposal.and(sideEffect: SE) = Effect(this, listOf(sideEffect))

}

data class Effect<Proposal, SideEffect>(
    val proposal: Proposal,
    val sideEffects: List<SideEffect> = emptyList()
) {

    @BlocDSL
    operator fun plus(sideEffect: SideEffect): Effect<Proposal, SideEffect> = Effect(proposal, sideEffects + sideEffect)
    @BlocDSL
    infix fun and(sideEffect: SideEffect): Effect<Proposal, SideEffect> = Effect(proposal, sideEffects + sideEffect)
}


