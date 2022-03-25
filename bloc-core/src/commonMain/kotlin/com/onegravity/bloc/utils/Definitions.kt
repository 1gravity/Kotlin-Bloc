package com.onegravity.bloc.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import org.reduxkotlin.GetState

@DslMarker
annotation class BlocDSL

@RequiresOptIn(message = "This is an internal API designed for Bloc extensions.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class BlocProtected

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class BlocInternal

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
typealias SideEffectStream<Value> = Flow<Value>

/**
 * A function for accepting / rejecting a [Proposal] and updating and emitting resulting [State].
 */
typealias Acceptor<Proposal, State> = (proposal: Proposal, state: State) -> State

typealias Mapper<Model, State> = (model: Model) -> State

typealias Selector<State, Model> = (State) -> Model

typealias Dispatcher<Action> = suspend (Action) -> Unit

data class InitializerContext<State, Action>(
    val state: State,
    val dispatch: Dispatcher<Action>
)

typealias Initializer<State, Action> = suspend InitializerContext<State, Action>.() -> Unit

data class ThunkContext<State, Action, A : Action>(
    val getState: GetState<State>,
    val action: A,
    val dispatch: Dispatcher<Action>
)

typealias Thunk<State, Action, A> = suspend ThunkContext<State, Action, A>.() -> Unit

data class ThunkContextNoAction<State, Action>(
    val getState: GetState<State>,
    val dispatch: Dispatcher<Action>
)

typealias ThunkNoAction<State, Action> = suspend ThunkContextNoAction<State, Action>.() -> Unit

data class ReducerContext<State, Action>(
    val state: State,
    val action: Action,
    // we need the CoroutineScope to use suspend functions in Redux Thunks
    // the CoroutineScope is the same used in the Bloc itself --> it's tied to BlocContext.lifecycle
    val coroutineScope: CoroutineScope
)

typealias Reducer<State, Action, Proposal> = suspend ReducerContext<State, Action>.() -> Proposal

data class ReducerContextNoAction<State>(val state: State, val scope: CoroutineScope)

typealias ReducerNoAction<State, Proposal> = suspend ReducerContextNoAction<State>.() -> Proposal

typealias SideEffect<State, Action, SideEffect> = ReducerContext<State, Action>.() -> SideEffect

typealias SideEffectNoAction<State, SideEffect> = ReducerContextNoAction<State>.() -> SideEffect
