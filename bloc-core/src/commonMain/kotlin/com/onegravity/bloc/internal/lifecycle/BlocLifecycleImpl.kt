package com.onegravity.bloc.internal.lifecycle

import com.arkivanov.essenty.lifecycle.*
import com.onegravity.bloc.internal.lifecycle.LifecycleState.*

/**
 * BlocLifecycle tied to an Essenty Lifecycle which is the "external" lifecycle a Bloc follows.
 * The BlocLifecycle is implemented using a finite state machine under the hood.
 */
internal class BlocLifecycleImpl(essentyLifecycle: Lifecycle) : BlocLifecycle {

    private var callbacks = emptySet<Callbacks>()

    private val stateMachine = LifecycleStateMachine { transition ->
        // translate BlocLifecycle lifecycle events to Callbacks
        when (transition.to) {
            Created -> callbacks.forEach(Callbacks::onCreate)
                .also { callbackSet += CallbackElement.OnCreate }
            Initializing -> callbacks.forEach(Callbacks::onInitialize)
                .also { callbackSet += CallbackElement.OnInitialize }
            InitializingStarting -> if (transition.from == Starting) callbacks.forEach(Callbacks::onInitialize)
                .also { callbackSet += CallbackElement.OnInitialize }
            Started -> callbacks.forEach(Callbacks::onStart)
                .also { callbackSet += CallbackElement.OnStart }
            Stopped -> callbacks.forEach(Callbacks::onStop)
                .also { callbackSet += CallbackElement.OnStop }
            Destroyed -> callbacks.forEach(Callbacks::onDestroy)
                .also { callbackSet -= CallbackElement.OnDestroy }
            else -> { /* NOP */ }
        }
    }

    private enum class CallbackElement {
        OnCreate, OnInitialize, OnStart, OnStop, OnDestroy
    }
    private val callbackSet = HashSet<CallbackElement>()

    init {
        // translate Essenty lifecycle to BlocLifecycle events
        essentyLifecycle.doOnCreate { stateMachine.transition(LifecycleEvent.Create) }
        essentyLifecycle.doOnStart { stateMachine.transition(LifecycleEvent.Start) }
        essentyLifecycle.doOnStop { stateMachine.transition(LifecycleEvent.Stop) }
        essentyLifecycle.doOnDestroy { stateMachine.transition(LifecycleEvent.Destroy) }
    }

    override fun subscribe(callbacks: Callbacks) {
        check(callbacks !in this.callbacks) { "Already subscribed" }

        if (callbackSet.contains(CallbackElement.OnCreate)) callbacks.onCreate()
        if (callbackSet.contains(CallbackElement.OnInitialize)) callbacks.onInitialize()
        if (callbackSet.contains(CallbackElement.OnStart)) callbacks.onStart()
        if (callbackSet.contains(CallbackElement.OnStop)) callbacks.onStop()
        if (callbackSet.contains(CallbackElement.OnDestroy)) callbacks.onDestroy()

        this.callbacks += callbacks
    }

    override fun unsubscribe(callbacks: Callbacks) {
        this.callbacks -= callbacks
    }

    override fun initializerStarting() {
        stateMachine.transition(LifecycleEvent.StartInitializer)
    }

    override fun initializerCompleted() {
        stateMachine.transition(LifecycleEvent.InitializerCompleted)
    }

    override fun isStarted() = stateMachine.state == Started

    override fun isStarting() = stateMachine.state.let { it == InitializingStarting || it == Initializing }

}
