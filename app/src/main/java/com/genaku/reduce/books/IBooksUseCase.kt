package com.genaku.reduce.books

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface IBooksUseCase {
    val state: StateFlow<BooksState>

    fun start(coroutineScope: CoroutineScope)
    fun load()
    fun clear()
}