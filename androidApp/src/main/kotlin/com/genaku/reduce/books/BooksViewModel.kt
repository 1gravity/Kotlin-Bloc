package com.genaku.reduce.books

import com.genaku.reduce.BaseViewModel
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.sample.books.BooksRepository
import com.onegravity.knot.sample.books.BooksUseCase
import com.onegravity.knot.sample.booksRedux.IBooksUseCase

class BooksViewModel(context: ActivityKnotContext) : BaseViewModel(context), IBooksUseCase {

    private val useCase: IBooksUseCase = BooksUseCase(viewModelContext, BooksRepository())

    override val state = useCase.state

    override fun load() {
        useCase.load()
    }

    override fun clear() {
        useCase.clear()
    }
}