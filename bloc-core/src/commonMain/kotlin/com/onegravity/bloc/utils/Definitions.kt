package com.onegravity.bloc.utils

import org.reduxkotlin.GetState

@DslMarker
annotation class BlocDSL

@RequiresOptIn(message = "This is an internal API designed for Bloc extensions.")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class BlocInternal

/**
 * A function for accepting / rejecting a [Proposal] and updating and emitting resulting [State].
 */
typealias Acceptor<Proposal, State> = (proposal: Proposal, state: State) -> State

typealias Mapper<Model, State> = (model: Model) -> State

typealias Selector<State, Model> = (State) -> Model

typealias Dispatcher<Action> = suspend (Action) -> Unit

data class ThunkContext<State, Action>(val getState: GetState<State>, val action: Action, val dispatch: Dispatcher<Action>)

typealias Thunk<State, Action> = suspend ThunkContext<State, Action>.() -> Unit

data class ReducerContext<State, Action>(val state: State, val action: Action)

typealias Reducer<State, Action, Proposal> = ReducerContext<State, Action>.() -> Proposal
