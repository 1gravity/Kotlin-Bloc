package com.onegravity.bloc.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

@DslMarker
public annotation class BlocDSL

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
internal annotation class BlocInternal

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

public typealias Initializer<State, Action> = suspend InitializerContext<State, Action>.() -> Unit

public typealias Thunk<State, Action, A> = suspend ThunkContext<State, Action, A>.() -> Unit

public typealias GetState<State> = () -> State

public typealias ThunkNoAction<State, Action> = suspend ThunkContextNoAction<State, Action>.() -> Unit

public typealias Reducer<State, Action, Proposal> = ReducerContext<State, Action>.() -> Proposal

public typealias CoroutineBlock = suspend CoroutineScope.() -> Unit

public typealias ReducerNoAction<State, Proposal> = ReducerContextNoAction<State>.() -> Proposal

public typealias SideEffect<State, Action, SideEffect> = ReducerContext<State, Action>.() -> SideEffect

public typealias SideEffectNoAction<State, SideEffect> = ReducerContextNoAction<State>.() -> SideEffect
