package com.onegravity.bloc.internal.builder

import com.onegravity.bloc.Bloc
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.internal.BlocImpl
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.utils.*
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName

/**
 * The BlocBuilder class to build them all.
 * This is what we use to build blocs using the BlocBuilderDsl.
 *
 * It has to be a class because of the reified functions and it has to be public because it's
 * exposed by the BlocBuilderDsl.
 */
public class BlocBuilder<State : Any, Action : Any, SE : Any, Proposal : Any> {

    private var _initialize: Initializer<State, Action>? = null
    private val _thunks = ArrayList<MatcherThunk<State, Action, Action>>()
    private val _reducers = ArrayList<MatcherReducer<State, Action, Effect<Proposal, SE>>>()
    private var _initDispatcher: CoroutineContext = Dispatchers.Default
    private var _thunkDispatcher: CoroutineContext = Dispatchers.Default
    private var _reduceDispatcher: CoroutineContext = Dispatchers.Default

    internal fun build(
        context: BlocContext,
        blocState: BlocState<State, Proposal>
    ): Bloc<State, Action, SE> = BlocImpl(
        blocContext = context,
        blocState = blocState,
        initialize = _initialize,
        thunks = _thunks,
        reducers = _reducers,
        initDispatcher = _initDispatcher,
        thunkDispatcher = _thunkDispatcher,
        reduceDispatcher = _reduceDispatcher,
    )

    /**
     * Create an initializer (onCreate { })
     */
    @BlocDSL
    public fun onCreate(initialize: Initializer<State, Action>) {
        when (_initialize) {
            null -> _initialize = initialize
            else -> logger.w("Initializer already defined -> ignoring this one")
        }
    }

    /**
     * Create a catch-all thunk (thunk { })
     */
    @BlocDSL
    public fun thunk(thunk: Thunk<State, Action, Action>) {
        _thunks.add(MatcherThunk(null, thunk))
    }

    /**
     * Create an action specific thunk (thunk(action) { })
     */
    @BlocDSL
    @JvmName("thunkMatching")
    public inline fun <reified A : Action> thunk(noinline thunk: Thunk<State, Action, A>) {
        addThunk(Matcher.any(), thunk)
    }

    /**
     * Create an action specific thunk (thunk(action) { })
     */
    @BlocDSL
    @JvmName("thunkMatchingEnums")
    @Suppress("UNCHECKED_CAST")
    public inline fun <reified ActionEnum : Enum<ActionEnum>, reified A : ActionEnum> thunk(
        childClazz: A,
        noinline thunk: Thunk<State, ActionEnum, A>
    ) {
        addThunk(
            Matcher.eq(childClazz) as Matcher<Action, Action>,
            thunk as Thunk<State, Action, Action>
        )
    }

    /**
     * Only used internally but needs to be public because of calls from inlined public functions
     */
    @BlocInternal
    @Suppress("UNCHECKED_CAST")
    public fun <A : Action> addThunk(matcher: Matcher<Action, A>, thunk: Thunk<State, Action, A>) {
        _thunks.add(MatcherThunk(matcher, thunk as Thunk<State, Action, Action>))
    }

    /**
     * Create a catch-all reducer (reduce { })
     */
    @BlocDSL
    public fun reduce(reducer: Reducer<State, Action, Proposal>) {
        val reducerNoSideEffect: Reducer<State, Action, Effect<Proposal, SE>> = {
            reducer.invoke(this).noSideEffect()
        }
        _reducers.add(MatcherReducer(null, reducerNoSideEffect, true))
    }

    /**
     * Create an action specific reducer (reduce<action> { })
     */
    @BlocDSL
    @JvmName("reduceMatching")
    @Suppress("UNCHECKED_CAST")
    public inline fun <reified A : Action> reduce(noinline reducer: Reducer<State, A, Proposal>) {
        val reducerNoSideEffect: Reducer<State, A, Effect<Proposal, SE>> = {
            reducer.invoke(this).noSideEffect()
        }
        addReducer(
            Matcher.any<Action, A>(),
            reducerNoSideEffect as Reducer<State, Action, Effect<Proposal, SE>>,
            true
        )
    }

    /**
     * Create an action specific reducer (reduce(action) { })
     */
    @BlocDSL
    @JvmName("reduceMatchingEnums")
    @Suppress("UNCHECKED_CAST")
    public inline fun <reified ActionEnum : Enum<ActionEnum>, reified A : ActionEnum> reduce(
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

    /**
     * Create a catch-all side-effect (sideEffect { })
     */
    @BlocDSL
    public fun sideEffect(sideEffect: SideEffect<State, Action, SE>) {
        val reducerNoState: Reducer<State, Action, Effect<Proposal, SE>> = {
            Effect(null, sideEffect.invoke(this))
        }
        _reducers.add(MatcherReducer(null, reducerNoState, false))
    }

    /**
     * Create an action specific side-effect (sideEffect<action> { })
     */
    @BlocDSL
    @JvmName("sideEffectMatching")
    @Suppress("UNCHECKED_CAST")
    public inline fun <reified A : Action> sideEffect(noinline sideEffect: SideEffect<State, A, SE>) {
        val reducerNoState: Reducer<State, A, Effect<Proposal, SE>> = {
            Effect(null, sideEffect.invoke(this))
        }
        addReducer(
            Matcher.any<Action, A>(),
            reducerNoState as Reducer<State, Action, Effect<Proposal, SE>>,
            false
        )
    }

    /**
     * Create an action specific side-effect (sideEffect(action) { })
     */
    @BlocDSL
    @JvmName("sideEffectMatchingEnums")
    @Suppress("UNCHECKED_CAST")
    public inline fun <reified ActionEnum : Enum<ActionEnum>, reified A : ActionEnum> sideEffect(
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

    /**
     * Create a catch-all reducer with side-effect(s) (reduceAnd { })
     */
    @BlocDSL
    public fun reduceAnd(reducer: Reducer<State, Action, Effect<Proposal, SE>>) {
        _reducers.add(MatcherReducer(null, reducer, true))
    }

    /**
     * Create an action specific reducer with side-effect(s) (reduceAnd<action> { })
     */
    @BlocDSL
    @JvmName("reduceWithSideEffectMatching")
    public inline fun <reified A : Action> reduceAnd(
        noinline reducer: Reducer<State, Action, Effect<Proposal, SE>>
    ) {
        addReducer(Matcher.any<Action, A>(), reducer, true)
    }

    /**
     * Create an action specific reducer with side-effect(s) (reduceAnd(action) { })
     */
    @BlocDSL
    @JvmName("reduceWithSideEffectMatchingEnums")
    @Suppress("UNCHECKED_CAST")
    public inline fun <reified ActionEnum : Enum<ActionEnum>, reified A : ActionEnum> reduceAnd(
        childClazz: A,
        noinline reducer: Reducer<State, Action, Effect<Proposal, SE>>
    ) {
        addReducer(
            Matcher.eq(childClazz) as Matcher<Action, Action>,
            reducer,
            true
        )
    }

    /**
     * Only used internally but needs to be public because of calls from inlined public functions
     */
    @BlocInternal
    public fun <A : Action> addReducer(
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
    public var dispatchers: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _initDispatcher = value
            _thunkDispatcher = value
            _reduceDispatcher = value
        }

    @BlocDSL
    public var initDispatcher: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _initDispatcher = value
        }

    @BlocDSL
    public var thunkDispatcher: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _thunkDispatcher = value
        }

    @BlocDSL
    public var reduceDispatcher: CoroutineContext = Dispatchers.Default
        set(value) {
            field = value
            _reduceDispatcher = value
        }

    /* Extension functions */

    /**
     * Used in a reducer with sideEffect to return state without side effect:
     * ```
     * reduceAnd {
     *    state.noSideEffect()
     * }
     */
    @BlocDSL
    public fun Proposal.noSideEffect(): Effect<Proposal, SE> = Effect(this, emptyList())

    /**
     * Used in a reducer with sideEffect to return a side effect:
     * ```
     * reduceAnd {
     *    state and sideEffect
     * }
     */
    @BlocDSL
    @JvmName("proposalAnd")
    public infix fun Proposal.and(sideEffect: SE): Effect<Proposal, SE> = Effect(this, sideEffect)

    /**
     * Used in a reducer with sideEffect to return a side effect:
     * ```
     * sideEffect {
     *    sideEffect and state
     * }
     */
    @BlocDSL
    @JvmName("sideEffectAnd")
    public infix fun SE.and(proposal: Proposal): Effect<Proposal, SE> = Effect(proposal, this)

    /**
     * Used in a reducer with sideEffect to return another side effect:
     * ```
     * reduceAnd {
     *    state and sideEffect1 and sideEffect2
     * }
     */
    @BlocDSL
    @JvmName("sideEffectAndSideEffect")
    public infix fun SE.and(sideEffect: SE): List<SE> = listOf(this, sideEffect)

}
