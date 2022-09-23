package com.onegravity.bloc.internal.lifecycle

internal sealed class LifecycleState(private val name: String) {
    object InitialState : LifecycleState("InitialState")
    object Created : LifecycleState("Created")
    object Initializing : LifecycleState("Initializing")
    object Initialized : LifecycleState("Initialized")
    object InitializingStarting : LifecycleState("InitializingStarting")
    object Starting : LifecycleState("Starting")
    object Started : LifecycleState("Started")
    object Stopped : LifecycleState("Stopped")
    object Destroyed : LifecycleState("Destroyed")

    override fun toString() = name
}
