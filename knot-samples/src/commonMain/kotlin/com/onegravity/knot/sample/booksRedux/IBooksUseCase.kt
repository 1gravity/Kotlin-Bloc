package com.onegravity.knot.sample.booksRedux

import com.onegravity.knot.Stream
import com.onegravity.knot.sample.books.BooksState

interface IBooksUseCase {
    val state: Stream<BooksState>

    fun load()
    fun clear()
}