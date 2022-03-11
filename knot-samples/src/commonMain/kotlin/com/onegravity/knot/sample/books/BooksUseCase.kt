package com.onegravity.knot.sample.books

import com.onegravity.knot.Stream

interface BooksUseCase {
    val state: Stream<BookState>

    fun load()
    fun clear()
}