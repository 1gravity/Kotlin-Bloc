package com.onegravity.bloc.utils

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * A MutableSharedFlow with
 * - an initial value
 * - no duplicates
 * - value property
 *
 * We use this "enhanced" MutableSharedFlow over MutableStateFlow because the latter conflates
 * values.
 * See: https://stackoverflow.com/questions/73583072/kotlin-flow-with-initial-value-replay-1-no-duplicates-no-conflation
 */
internal class MutableBehaviorFlow<T>(
    private val initialValue: T,
    private val backingSharedFlow: MutableSharedFlow<T> =
        MutableSharedFlow(replay = 1, extraBufferCapacity = Int.MAX_VALUE, BufferOverflow.SUSPEND)
) : MutableSharedFlow<T> by backingSharedFlow {

    init {
        backingSharedFlow.tryEmit(initialValue)
    }

    val value: T
        get() = runCatching { replayCache.last() }.getOrDefault(initialValue)

    override suspend fun emit(value: T) {
        if (value != this.value) backingSharedFlow.emit(value)
    }

    override fun tryEmit(value: T) =
        if (value != this.value) {
            backingSharedFlow.tryEmit(value)
        } else true
}
