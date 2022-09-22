package com.onegravity.bloc.internal.lifecycle

/**
 * Convenience extension function to subscribe to onCreate events
 */
internal inline fun BlocLifecycle.doOnCreate(crossinline block: () -> Unit) {
    subscribe(object : Callbacks {
        override fun onCreate() {
            unsubscribe(this)
            block()
        }
    })
}

/**
 * Convenience extension function to subscribe to onInitialize events
 */
internal inline fun BlocLifecycle.doOnInitialize(crossinline block: () -> Unit) {
    subscribe(object : Callbacks {
        override fun onInitialize() {
            unsubscribe(this)
            block()
        }
    })
}

/**
 * Convenience extension function to subscribe to onStart events
 */
internal inline fun BlocLifecycle.doOnStart(isOneTime: Boolean = false, crossinline block: () -> Unit) {
    subscribe(object : Callbacks {
        override fun onStart() {
            if (isOneTime) unsubscribe(this)
            block()
        }
    })
}

/**
 * Convenience extension function to subscribe to onStop events
 */
internal inline fun BlocLifecycle.doOnStop(isOneTime: Boolean = false, crossinline block: () -> Unit) {
    subscribe(object : Callbacks {
        override fun onStop() {
            if (isOneTime) unsubscribe(this)
            block()
        }
    })
}

/**
 * Convenience extension function to subscribe to onDestroy events
 */
internal inline fun BlocLifecycle.doOnDestroy(crossinline block: () -> Unit) {
    subscribe(object : Callbacks {
        override fun onDestroy() {
            block()
        }
    })
}
