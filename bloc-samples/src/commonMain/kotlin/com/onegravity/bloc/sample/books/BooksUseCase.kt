package com.onegravity.bloc.sample.books

import com.onegravity.bloc.utils.BlocObservableOwner

interface BooksUseCase : BlocObservableOwner<BookState, Nothing> {
    fun load()
    fun clear()
}