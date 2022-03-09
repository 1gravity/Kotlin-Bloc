package com.onegravity.knot.sample.books

import com.onegravity.knot.Stream
import kotlinx.coroutines.CoroutineScope

interface IBooksUseCase {
    val state: Stream<BooksState>

    fun load()
    fun clear()
}