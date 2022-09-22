package com.onegravity.bloc.internal.lifecycle

internal interface BlocLifecycle {

    fun subscribe(callbacks: Callbacks)

    fun unsubscribe(callbacks: Callbacks)

    fun initializerStarting()

    fun initializerCompleted()

}