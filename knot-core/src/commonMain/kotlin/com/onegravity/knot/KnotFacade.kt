package com.onegravity.knot

/**
 * The KnotFacade is the ui facing part of the Knot
 */
interface KnotFacade<out State, in Action> : Stream<State>, Sink<Action>

interface KnotState<out State, in Action> : KnotFacade<State, Action>

interface Knot<out State, in Action, Model, SideEffect> : KnotState<State, Action>
