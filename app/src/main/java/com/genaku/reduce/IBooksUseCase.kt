package com.genaku.reduce

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface IBooksUseCase {
    val state: StateFlow<State>

    fun start(coroutineScope: CoroutineScope)
    fun load()
    fun clear()
}