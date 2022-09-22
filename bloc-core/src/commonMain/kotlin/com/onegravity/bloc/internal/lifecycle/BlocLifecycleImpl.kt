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
            InitialState -> { /* NOP */ }
            Created -> callbacks.forEach(Callbacks::onCreate)
            Initializing -> callbacks.forEach(Callbacks::onInitialize)
            Initialized -> { /* NOP */ }
            Started -> callbacks.forEach(Callbacks::onStart)
            Stopped -> callbacks.forEach(Callbacks::onStop)
            Destroyed -> callbacks.forEach(Callbacks::onDestroy)
        }
    }

    init {
        // translate Essenty lifecycle to BlocLifecycle events
        essentyLifecycle.doOnCreate { stateMachine.transition(LifecycleEvent.Create) }
        essentyLifecycle.doOnStart { stateMachine.transition(LifecycleEvent.Start) }
        essentyLifecycle.doOnStop { stateMachine.transition(LifecycleEvent.Stop) }
        essentyLifecycle.doOnDestroy { stateMachine.transition(LifecycleEvent.Destroy) }
    }

    override fun subscribe(callbacks: Callbacks) {
        check(callbacks !in this.callbacks) { "Already subscribed" }

        this.callbacks += callbacks

        val position = stateMachine.state.pos
        if (position >= Created.pos) {
            callbacks.onCreate()
        }
        if (position >= Started.pos) {
            callbacks.onStart()
        }
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
}
