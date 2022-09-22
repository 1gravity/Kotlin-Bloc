package com.onegravity.bloc.internal.lifecycle

internal sealed class LifecycleState(val pos: Int, private val name: String) {
    object InitialState : LifecycleState(0, "InitialState")
    object Created : LifecycleState(1, "Created")
    object Initializing : LifecycleState(2, "Initializing")
    object Initialized : LifecycleState(2, "Initialized")
    object Started : LifecycleState(3, "Started")
    object Stopped : LifecycleState(4, "Stopped")
    object Destroyed : LifecycleState(5, "Destroyed")

    override fun toString() = name
}
