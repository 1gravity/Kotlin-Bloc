package com.onegravity.bloc.internal.lifecycle

internal sealed class LifecycleState {
    object InitialState : LifecycleState()
    object Created : LifecycleState()
    object RunInitializer : LifecycleState()
    object Started : LifecycleState()
    object Stopped : LifecycleState()
    object Destroyed : LifecycleState()
}
