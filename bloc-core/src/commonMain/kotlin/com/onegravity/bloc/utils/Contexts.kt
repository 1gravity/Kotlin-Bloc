package com.onegravity.bloc.utils

/**
 * [InitializerContext] is used as receiver for an initializer:
 * ```
 *   onCreate {
 *       // use state
 *       // dispatch(action)
 *   }
 * ```
 */
public data class InitializerContext<State, Action>(
    val state: State,
    val dispatch: Dispatcher<Action>,
    internal val runner: CoroutineRunner
)

/**
 * [ThunkContext] is used as receiver for the thunk when defining it with an action:
 * ```
 *   thunk<A: Action> {
 *   }
 *
 *   // or
 *
 *   thunk(EnumAction) {
 *   }
 * ```
 */
public data class ThunkContext<State, Action, A : Action>(
    val getState: GetState<State>,
    val action: A,
    val dispatch: Dispatcher<Action>,
    internal val runner: CoroutineRunner
)

/**
 * [ThunkContextNoAction] is used as receiver for the thunk defined MVVM+ style (no action):
 * ```
 *   fun doSomething() = thunk {
 *   }
 * ```
 */
public data class ThunkContextNoAction<State, Action>(
    val getState: GetState<State>,
    val dispatch: Dispatcher<Action>,
    internal val runner: CoroutineRunner
)

/**
 * [ReducerContext] is used as receiver for the receiver when defining it with an action:
 * ```
 *   reduce<A: Action> {
 *   }
 *
 *   // or
 *
 *   reduce(EnumAction) {
 *   }
 * ```
 */
public data class ReducerContext<State, Action>(
    val state: State,
    val action: Action,
    internal val runner: CoroutineRunner
)

/**
 * [ReducerContextNoAction] is used as receiver for the receiver defined MVVM+ style (no action):
 * ```
 *   fun doSomething() = reduce {
 *   }
 * ```
 */
public class ReducerContextNoAction<State>(
    public val state: State,
    internal val runner: CoroutineRunner
)
