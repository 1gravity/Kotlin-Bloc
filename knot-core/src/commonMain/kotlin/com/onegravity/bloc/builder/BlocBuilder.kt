package com.onegravity.bloc.builder

import com.onegravity.bloc.*
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.Matcher
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class BlocBuilder<State, Action: Any, Proposal> {

    private var _thunks: MutableList<Thunk<State, Action>> = ArrayList()
    private val _actionThunks: MutableMap<Matcher<Action, Action>, Thunk<State, Action>> = LinkedHashMap()
    private var _reducer: Reducer<State, Action, Proposal>? = null
    private var _dispatcher: CoroutineContext = Dispatchers.Default

    fun build(context: BlocContext, blocState: BlocState<State, Proposal>) = BlocImpl(
        context = context,
        blocState = blocState,
        reducer = checkNotNull(_reducer) { "reduce { } must be declared" },
        thunks = _thunks,
        actionThunks = _actionThunks,
        dispatcher = _dispatcher,
    )

    fun thunk(thunk: Thunk<State, Action>) {
        _thunks.add(thunk)
    }

    inline fun <reified A : Action> thunkMatching(noinline thunk: Thunk<State, Action>) {
        action(Matcher.any<Action, A>(), thunk)
    }

    fun <A : Action> action(
        stateMatcher: Matcher<Action, A>,
        thunk: Thunk<State, Action>
    ) {
        _actionThunks[stateMatcher] = thunk
    }

    // TODO implement reduce<Action> { }
    fun reduce(reducer: Reducer<State, Action, Proposal>) {
        check(_reducer == null) { "only one reduce { } can be defined" }
        _reducer = reducer
    }

    var dispatcher: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _dispatcher = value
        }

}
