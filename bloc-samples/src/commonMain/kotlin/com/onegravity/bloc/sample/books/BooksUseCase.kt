package com.onegravity.bloc.sample.books

import com.onegravity.bloc.utils.Stream

interface BooksUseCase {
    val state: Stream<BookState>

    fun load()
    fun clear()
}