package com.onegravity.bloc.utils

/**
 * [InitializerContext] is used as receiver for an initializer:
 * ```
 *   onCreate {
 *       // use state
 *       // dispatch(action)
 *   }
 * ```
 *
 * @param getState returns the current state
 * @param dispatch dispatches an action
 * @param reduce reduces a proposal
 * @param launchBlock launch a coroutine without exposing the bloc's CoroutineScope,
 *                    it's internal to allow for JobConfig default values via extension functions
 */
public data class InitializerContext<State, Action, Proposal>(
    val getState: GetState<State>,
    val dispatch: Dispatcher<Action>,
    val reduce: suspend (proposal: Proposal) -> Unit,
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
 *
 * @param getState returns the current state
 * @param action the action that triggered the thunk
 * @param dispatch dispatches an action
 * @param reduce reduces a proposal
 * @param launchBlock launch a coroutine without exposing the bloc's CoroutineScope,
 *                    it's internal to allow for JobConfig default values via extension functions
 */
public data class ThunkContext<State, Action, A : Action, Proposal>(
    val getState: GetState<State>,
    val action: A,
    val dispatch: Dispatcher<Action>,
    val reduce: suspend (proposal: Proposal) -> Unit,
    internal val launchBlock: Launch
)

/**
 * [ThunkContextNoAction] is used as receiver for the thunk defined MVVM+ style (no action):
 * ```
 *   fun doSomething() = thunk {
 *   }
 * ```
 *
 * @param getState returns the current state
 * @param dispatch dispatches an action
 * @param reduce reduces a proposal
 * @param launchBlock launch a coroutine without exposing the bloc's CoroutineScope,
 *                    it's internal to allow for JobConfig default values via extension functions
 */
public data class ThunkContextNoAction<State, Action, Proposal>(
    val getState: GetState<State>,
    val dispatch: Dispatcher<Action>,
    val reduce: suspend (proposal: Proposal) -> Unit,
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
 *
 * @param state the current state
 * @param action the action that triggered the reducer
 * @param launchBlock launch a coroutine without exposing the bloc's CoroutineScope,
 *                    it's internal to allow for JobConfig default values via extension functions
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
 *
 * @param state the current state
 * @param launchBlock launch a coroutine without exposing the bloc's CoroutineScope,
 *                    it's internal to allow for JobConfig default values via extension functions
 */
public data class ReducerContextNoAction<State>(
    val state: State,
    internal val launchBlock: Launch
)
