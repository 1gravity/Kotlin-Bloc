package com.onegravity.bloc.internal.lifecycle

internal fun BlocLifecycle.subscribe(
    onCreate: (() -> Unit)? = null,
    onInitialize: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null
) = object : Callbacks {
    override fun onCreate() {
        onCreate?.invoke()
    }

    override fun onInitialize() {
        onInitialize?.invoke()
    }

    override fun onStart() {
        onStart?.invoke()
    }

    override fun onStop() {
        onStop?.invoke()
    }

    override fun onDestroy() {
        onDestroy?.invoke()
    }
}.also(::subscribe)
