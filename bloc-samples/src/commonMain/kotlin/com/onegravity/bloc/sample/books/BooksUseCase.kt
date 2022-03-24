package com.onegravity.bloc.sample.books

import com.onegravity.bloc.utils.BlocObservableOwner

interface BooksUseCase : BlocObservableOwner<BookState, Unit> {
    fun load()
    fun clear()
}