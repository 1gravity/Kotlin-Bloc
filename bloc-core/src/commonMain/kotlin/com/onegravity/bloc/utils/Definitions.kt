// TODO update all the documentation for the different COntext objects that still mention th
//      CoroutineScope which is internal now

package com.onegravity.bloc.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow

@DslMarker
public annotation class BlocDSL

@RequiresOptIn(message = "This is an internal API designed for Bloc extensions.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
public annotation class BlocProtected

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
public annotation class BlocInternal

/**
 * This is the Observer specifically for Swift
 */
public typealias BlocObserver<State> = (State) -> Unit

/**
 * A SideEffectStream is a source of asynchronous (side effect) data.
 * It's a hot stream meant to deal with SideEffect data (compared to StateStream for State).
 *
 * A SideEffectStream emits:
 * - all values even duplicates
 * - no initial value upon subscription (analogous PublishSubject)
 *
 * A StateStream emits:
 * - no duplicate values
 * - an initial value upon subscription (analogous BehaviorSubject)
 */
public typealias SideEffectStream<Value> = Flow<Value>

/**
 * Function for accepting / rejecting a Proposal.
 * If the proposal is accepted, returns the new State (which is based on the Proposal).
 * If the proposal is rejected, returns Null.
 */
public typealias Acceptor<Proposal, State> = (proposal: Proposal, state: State) -> State?

public typealias Mapper<Model, State> = (model: Model) -> State

public typealias Selector<State, Model> = (State) -> Model

public typealias Dispatcher<Action> = suspend (Action) -> Unit

public data class InitializerContext<State, Action>(
    val state: State,
    val dispatch: Dispatcher<Action>,
    // we need the CoroutineScope so we can launch jobs from an Initializer
    // the CoroutineScope is the same used in the Bloc itself --> it's tied to BlocContext.lifecycle
    internal val coroutineScope: CoroutineScope
)

public typealias Initializer<State, Action> = suspend InitializerContext<State, Action>.() -> Unit

/**
 * ThunkContext is used when defining a thunk like this:
 * ```
 *   thunk<A: Action> {
 *   }
 * ```
 */
public data class ThunkContext<State, Action, A : Action>(
    val getState: GetState<State>,
    val action: A,
    val dispatch: Dispatcher<Action>,
    // we need the CoroutineScope so we can launch jobs in a thunk
    // the CoroutineScope is the same used in the Bloc itself --> it's tied to BlocContext.lifecycle
    internal val coroutineScope: CoroutineScope
) {
    private val runner = CoroutineRunner()

    internal fun runSingle(jobConfig: JobConfig, block: suspend CoroutineScope.() -> Unit) {
        runner.runSingle(jobConfig, coroutineScope, block)
    }
}

public typealias Thunk<State, Action, A> = suspend ThunkContext<State, Action, A>.() -> Unit

public typealias GetState<State> = () -> State

/**
 * ThunkContextNoAction is used when defining a thunk like this (as part of a function call e.g.):
 * ```
 *   fun doSomething() = thunk {
 *   }
 * ```
 */
public data class ThunkContextNoAction<State, Action>(
    val getState: GetState<State>,
    val dispatch: Dispatcher<Action>,
    // we need the CoroutineScope so we can launch jobs in a thunk
    // the CoroutineScope is the same used in the Bloc itself --> it's tied to BlocContext.lifecycle
    internal val coroutineScope: CoroutineScope,
) {
    private val runner = CoroutineRunner()

    internal fun runSingle(jobConfig: JobConfig, block: suspend CoroutineScope.() -> Unit) {
        runner.runSingle(jobConfig, coroutineScope, block)
    }
}

public typealias ThunkNoAction<State, Action> = suspend ThunkContextNoAction<State, Action>.() -> Unit

public data class ReducerContext<State, Action>(
    val state: State,
    val action: Action,
    // we need the CoroutineScope so we can launch jobs in a reducer
    // the CoroutineScope is the same used in the Bloc itself --> it's tied to BlocContext.lifecycle
    internal val coroutineScope: CoroutineScope
) {
    private val runner = CoroutineRunner()

    internal fun runSingle(jobConfig: JobConfig, block: suspend CoroutineScope.() -> Unit) {
        runner.runSingle(jobConfig, coroutineScope, block)
    }
}

public typealias Reducer<State, Action, Proposal> = suspend ReducerContext<State, Action>.() -> Proposal

public class ReducerContextNoAction<State>(
    public val state: State,
    // we need the CoroutineScope so we can launch jobs in a reducer
    // the CoroutineScope is the same used in the Bloc itself --> it's tied to BlocContext.lifecycle
    internal val coroutineScope: CoroutineScope
) {
    private val runner = CoroutineRunner()

    internal fun runSingle(jobConfig: JobConfig, block: suspend CoroutineScope.() -> Unit) {
        runner.runSingle(jobConfig, coroutineScope, block)
    }
}

/**
 * This definition is experimental, might lead to naming conflicts
 */
public typealias Coroutine = suspend CoroutineScope.() -> Unit

public typealias ReducerNoAction<State, Proposal> = suspend ReducerContextNoAction<State>.() -> Proposal

public typealias SideEffect<State, Action, SideEffect> = ReducerContext<State, Action>.() -> SideEffect

public typealias SideEffectNoAction<State, SideEffect> = ReducerContextNoAction<State>.() -> SideEffect
