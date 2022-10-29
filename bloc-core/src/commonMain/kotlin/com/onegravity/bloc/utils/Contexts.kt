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
public data class InitializerContext<State, Action, Proposal>(
    val state: State,
    val dispatch: Dispatcher<Action>,
    val reduce: (proposal: Proposal) -> Unit,
    internal val launchBlock: Launch
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
public data class ThunkContext<State, Action, A : Action, Proposal>(
    val getState: GetState<State>,
    val action: A,
    val dispatch: Dispatcher<Action>,
    val reduce: (proposal: Proposal) -> Unit,
    internal val launchBlock: Launch
)

/**
 * [ThunkContextNoAction] is used as receiver for the thunk defined MVVM+ style (no action):
 * ```
 *   fun doSomething() = thunk {
 *   }
 * ```
 */
public data class ThunkContextNoAction<State, Action, Proposal>(
    val getState: GetState<State>,
    val dispatch: Dispatcher<Action>,
    val reduce: (proposal: Proposal) -> Unit,
    internal val launchBlock: Launch
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
    internal val launchBlock: Launch
)

/**
 * [ReducerContextNoAction] is used as receiver for the receiver defined MVVM+ style (no action):
 * ```
 *   fun doSomething() = reduce {
 *   }
 * ```
 */
public data class ReducerContextNoAction<State>(
    val state: State,
    internal val launchBlock: Launch
)
