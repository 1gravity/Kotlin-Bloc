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

/**
 * Function to map a (Redux) model to (bloc) state.
 * @see <a href="https://1gravity.github.io/Kotlin-Bloc/docs/extensions/redux/redux_motivation">
 *     Redux Extension</a>
 */
public typealias Mapper<Model, State> = (model: Model) -> State

/**
 * Function to select memoized sub-state from (Redux) state.
 * @see <a href="https://1gravity.github.io/Kotlin-Bloc/docs/extensions/redux/redux_motivation">
 *     Redux Extension</a>
 */
public typealias Selector<State, Model> = (State) -> Model

/**
 * Function to dispatch actions to a bloc. [Dispatcher]s are used by [Initializer]s and [Thunk]s or
 * [ThunkNoAction]s to dispatch an action to the  "next" thunk or reducer in the execution chain.
 */
public typealias Dispatcher<Action> = suspend (Action) -> Unit

/**
 * Function that is executed when the bloc is created.
 * @see <a href="https://1gravity.github.io/Kotlin-Bloc/docs/architecture/bloc/initializer">
 *     Initializer</a>
 */
public typealias Initializer<State, Action, Proposal> =
        suspend InitializerContext<State, Action, Proposal>.() -> Unit

/**
 * Function that runs asynchronous code.
 * @see <a href="https://1gravity.github.io/Kotlin-Bloc/docs/architecture/bloc/thunk">
 *     Thunk</a>
 */
public typealias Thunk<State, Action, A, Proposal> =
        suspend ThunkContext<State, Action, A, Proposal>.() -> Unit

/**
 * Function that runs asynchronous code.
 * @see <a href="https://1gravity.github.io/Kotlin-Bloc/docs/architecture/bloc/thunk">
 *     Thunk</a>
 */
public typealias ThunkNoAction<State, Action, Proposal> =
        suspend ThunkContextNoAction<State, Action, Proposal>.() -> Unit

/**
 * Function that returns the current state. [GetState] is used by thunks to retrieve the current
 * state of the bloc and is accessible through the [ThunkContext] / [ThunkContextNoAction].
 */
public typealias GetState<State> = () -> State

/**
 * Function that reduces state.
 * @see <a href="https://1gravity.github.io/Kotlin-Bloc/docs/architecture/bloc/reducer">
 *     Reducer</a>
 */
public typealias Reducer<State, Action, Proposal> = ReducerContext<State, Action>.() -> Proposal

/**
 * Function that reduces state.
 * @see <a href="https://1gravity.github.io/Kotlin-Bloc/docs/architecture/bloc/reducer">
 *     Reducer</a>
 */
public typealias ReducerNoAction<State, Proposal> = ReducerContextNoAction<State>.() -> Proposal

/**
 * Function that emits a side effect.
 * @see <a href="https://1gravity.github.io/Kotlin-Bloc/docs/architecture/bloc/reducer#side-effect">
 *     Side Effect</a>
 */
public typealias SideEffect<State, Action, SideEffect> = ReducerContext<State, Action>.() -> SideEffect

/**
 * Function that emits a side effect.
 * @see <a href="https://1gravity.github.io/Kotlin-Bloc/docs/architecture/bloc/reducer#side-effect">
 *     Side Effect</a>
 */
public typealias SideEffectNoAction<State, SideEffect> = ReducerContextNoAction<State>.() -> SideEffect

/**
 * Suspend function used as parameter in one of the launch function calls ([InitializerContext.launch],
 * [ThunkContext.launch] etc.).
 */
public typealias CoroutineBlock = suspend CoroutineScope.() -> Unit
