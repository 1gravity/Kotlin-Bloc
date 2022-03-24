package com.onegravity.bloc.utils

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
 * A Stream is a source of asynchronous data.
 * It's identical to kotlinx.coroutines.flow.Flow.
 */
typealias Stream<Value> = Flow<Value>

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

data class ThunkContext<State, Action, A: Action>(
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

data class ReducerContext<State, Action>(val state: State, val action: Action)

typealias Reducer<State, Action, Proposal> = suspend ReducerContext<State, Action>.() -> Proposal

data class ReducerContextNoAction<State>(val state: State)

typealias ReducerNoAction<State, Proposal> = suspend ReducerContextNoAction<State>.() -> Proposal

typealias SideEffect<State, Action, SideEffect> = ReducerContext<State, Action>.() -> SideEffect

typealias SideEffectNoAction<State, SideEffect> = ReducerContextNoAction<State>.() -> SideEffect
