package com.onegravity.bloc

import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

open class BlocBuilder<State, Action: Any, Proposal> {

    private val _thunks = ArrayList<MatcherThunk<State, Action>>()
    private val _reducers = ArrayList<MatcherReducer<State, Action, Proposal>>()
    private val _sideEffects = ArrayList<MatcherSideEffect<State, Action, Proposal>>()
    private var _dispatcher: CoroutineContext = Dispatchers.Default

    fun build(context: BlocContext, blocState: BlocState<State, Proposal>) = BlocImpl(
        context = context,
        blocState = blocState,
        reducers = _reducers,
        thunks = _thunks,
        dispatcher = _dispatcher,
    )

    @BlocDSL
    fun thunk(thunk: Thunk<State, Action>) {
        _thunks.add(MatcherThunk(null, thunk))
    }

    @BlocDSL
    @JvmName("thunkMatching")
    inline fun <reified A : Action> thunk(noinline thunk: Thunk<State, Action>) {
        addThunk(Matcher.any<Action, A>(), thunk)
    }

    fun <A : Action> addThunk(
        stateMatcher: Matcher<Action, A>,
        thunk: Thunk<State, Action>
    ) {
        _thunks.add(MatcherThunk(stateMatcher, thunk))
    }

    @BlocDSL
    fun reduce(reducer: Reducer<State, Action, Proposal>) {
        _reducers.add(MatcherReducer(null, reducer))
    }

    @BlocDSL
    @JvmName("reducerMatching")
    inline fun <reified A : Action> reduce(noinline reducer: Reducer<State, Action, Proposal>) {
        addReducer(Matcher.any<Action, A>(), reducer)
    }

    fun <A : Action> addReducer(
        stateMatcher: Matcher<Action, A>,
        reducer: Reducer<State, Action, Proposal>
    ) {
        _reducers
            .firstOrNull { it.matcher != null && it.matcher == stateMatcher }
            ?.run { logger.e("Duplicate reduce<${stateMatcher.clazzName()}>") }
        _reducers.add(MatcherReducer(stateMatcher, reducer))
    }

    @BlocDSL
    var dispatcher: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _dispatcher = value
        }

}
