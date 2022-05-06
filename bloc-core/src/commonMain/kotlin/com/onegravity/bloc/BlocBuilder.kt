package com.onegravity.bloc

import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.fsm.Matcher
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

class BlocBuilder<State : Any, Action : Any, SE : Any, Proposal : Any> {

    private var _initializer: Initializer<State, Action> = { }
    private val _thunks = ArrayList<MatcherThunk<State, Action, Action>>()
    private val _reducers = ArrayList<MatcherReducer<State, Action, Effect<Proposal, SE>>>()
    private var _dispatcher: CoroutineContext = Dispatchers.Default

    fun build(
        context: BlocContext,
        blocState: BlocState<State, Proposal>
    ): Bloc<State, Action, SE> = BlocImpl(
        blocContext = context,
        blocState = blocState,
        initializer = _initializer,
        thunks = _thunks,
        reducers = _reducers,
        dispatcher = _dispatcher,
    )

    /* *** Initialization *** */

    @BlocDSL
    fun onCreate(initializer: Initializer<State, Action>) {
        _initializer = initializer
    }

    /* *** Thunks *** */

    @BlocDSL
    fun thunk(thunk: Thunk<State, Action, Action>) {
        _thunks.add(MatcherThunk(null, thunk))
    }

    @BlocDSL
    @JvmName("thunkMatching")
    inline fun <reified A : Action> thunk(noinline thunk: Thunk<State, Action, A>) {
        addThunk(Matcher.any(), thunk)
    }

    @BlocDSL
    @JvmName("thunkMatchingEnums")
    @Suppress("UNCHECKED_CAST")
    inline fun <reified ActionEnum : Enum<ActionEnum>, reified A : ActionEnum> thunk(
        childClazz: A,
        noinline thunk: Thunk<State, ActionEnum, A>
    ) {
        addThunk(
            Matcher.eq(childClazz) as Matcher<Action, Action>,
            thunk as Thunk<State, Action, Action>
        )
    }

    @BlocInternal
    @Suppress("UNCHECKED_CAST")
    fun <A : Action> addThunk(matcher: Matcher<Action, A>, thunk: Thunk<State, Action, A>) {
        _thunks.add(MatcherThunk(matcher, thunk as Thunk<State, Action, Action>))
    }

    /* Reducer with state but without side effect(s) */

    @BlocDSL
    fun reduce(reducer: Reducer<State, Action, Proposal>) {
        val reducerNoSideEffect: Reducer<State, Action, Effect<Proposal, SE>> = {
            reducer.invoke(this).noSideEffect()
        }
        _reducers.add(MatcherReducer(null, reducerNoSideEffect, true))
    }

    @BlocDSL
    @JvmName("reduceMatching")
    @Suppress("UNCHECKED_CAST")
    inline fun <reified A : Action> reduce(noinline reducer: Reducer<State, A, Proposal>) {
        val reducerNoSideEffect: Reducer<State, A, Effect<Proposal, SE>> = {
            reducer.invoke(this).noSideEffect()
        }
        addReducer(
            Matcher.any<Action, A>(),
            reducerNoSideEffect as Reducer<State, Action, Effect<Proposal, SE>>,
            true
        )
    }

    @BlocDSL
    @JvmName("reduceMatchingEnums")
    @Suppress("UNCHECKED_CAST")
    inline fun <reified ActionEnum : Enum<ActionEnum>, reified A : ActionEnum> reduce(
        childClazz: A,
        noinline reducer: Reducer<State, A, Proposal>
    ) {
        val reducerNoSideEffect: Reducer<State, A, Effect<Proposal, SE>> = {
            reducer.invoke(this).noSideEffect()
        }
        addReducer(
            Matcher.eq(childClazz) as Matcher<Action, Action>,
            reducerNoSideEffect as Reducer<State, Action, Effect<Proposal, SE>>,
            true
        )
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
    @Suppress("UNCHECKED_CAST")
    inline fun <reified A : Action> sideEffect(noinline sideEffect: SideEffect<State, A, SE>) {
        val reducerNoState: Reducer<State, A, Effect<Proposal, SE>> = {
            Effect(null, sideEffect.invoke(this))
        }
        addReducer(
            Matcher.any<Action, A>(),
            reducerNoState as Reducer<State, Action, Effect<Proposal, SE>>,
            false
        )
    }

    @BlocDSL
    @JvmName("sideEffectMatchingEnums")
    @Suppress("UNCHECKED_CAST")
    inline fun <reified ActionEnum : Enum<ActionEnum>, reified A : ActionEnum> sideEffect(
        childClazz: A,
        noinline sideEffect: SideEffect<State, A, SE>
    ) {
        val reducerNoState: Reducer<State, A, Effect<Proposal, SE>> = {
            Effect(null, sideEffect.invoke(this))
        }
        addReducer(
            Matcher.eq(childClazz) as Matcher<Action, Action>,
            reducerNoState as Reducer<State, Action, Effect<Proposal, SE>>,
            false
        )
    }

    /* Reducers with state and side effect(s) */

    @BlocDSL
    fun reduceAnd(reducer: Reducer<State, Action, Effect<Proposal, SE>>) {
        _reducers.add(MatcherReducer(null, reducer, true))
    }

    @BlocDSL
    @JvmName("reduceWithSideEffectMatching")
    inline fun <reified A : Action> reduceAnd(
        noinline reducer: Reducer<State, Action, Effect<Proposal, SE>>
    ) {
        addReducer(Matcher.any<Action, A>(), reducer, true)
    }

    @BlocDSL
    @JvmName("reduceWithSideEffectMatchingEnums")
    @Suppress("UNCHECKED_CAST")
    inline fun <reified ActionEnum : Enum<ActionEnum>, reified A : ActionEnum> reduceAnd(
        childClazz: A,
        noinline reducer: Reducer<State, Action, Effect<Proposal, SE>>
    ) {
        addReducer(
            Matcher.eq(childClazz) as Matcher<Action, Action>,
            reducer,
            true
        )
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
    fun Proposal.noSideEffect() = Effect<Proposal, SE>(this, emptyList())

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
