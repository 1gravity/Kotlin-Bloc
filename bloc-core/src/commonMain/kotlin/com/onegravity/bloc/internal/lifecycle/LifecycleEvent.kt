package com.onegravity.bloc.internal.lifecycle

internal sealed class LifecycleEvent {
    object Create : LifecycleEvent()
    object InitializerStarting : LifecycleEvent()
    object InitializerCompleted : LifecycleEvent()
    object Start : LifecycleEvent()
    object Stop : LifecycleEvent()
    object Destroy : LifecycleEvent()
}