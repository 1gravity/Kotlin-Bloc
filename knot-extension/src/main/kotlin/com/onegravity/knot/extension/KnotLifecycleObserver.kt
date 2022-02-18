package com.onegravity.knot.extension

import androidx.lifecycle.*
import com.onegravity.knot.JobSwitcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import androidx.lifecycle.coroutineScope

class KnotLifecycleObserver(
    private val lifecycle: Lifecycle,
    private val jobSwitcher: JobSwitcher
) : LifecycleObserver, CoroutineScope by MainScope() {

    private val shouldBeActive
        get() = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)

    private var isActive: Boolean = false

    private val stateIsChanged: Boolean
        get() {
            val newActive = shouldBeActive
            // skip subsequent process if the active state is not changed
            // ex: STARTED -> RESUMED
            if (isActive == newActive) {
                return false
            }
            isActive = newActive
            return true
        }

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onStateChanged() {
        if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
            cancel()
            return
        }
        if (stateIsChanged) {
            if (isActive) {
                jobSwitcher.start(lifecycle.coroutineScope)
            } else {
                jobSwitcher.stop()
            }
        }
    }
}