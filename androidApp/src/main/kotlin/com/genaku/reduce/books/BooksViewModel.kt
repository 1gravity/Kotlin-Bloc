package com.genaku.reduce.books

import com.genaku.reduce.BaseViewModel
import com.onegravity.knot.ActivityKnotContext
import com.onegravity.knot.sample.books.BooksRepositoryImpl
import com.onegravity.knot.sample.books.BooksUseCaseImplRedux
import com.onegravity.knot.sample.books.BooksUseCase
import com.onegravity.knot.sample.books.BooksUseCaseImpl

class BooksViewModel(context: ActivityKnotContext) : BaseViewModel(context), BooksUseCase {

//    private val useCase: BooksUseCase = BooksUseCaseImplRedux(viewModelContext)
    private val useCase: BooksUseCase = BooksUseCaseImpl(viewModelContext, BooksRepositoryImpl())

    override val state
        get() = useCase.state

    override fun load() {
        useCase.load()
    }

    override fun clear() {
        useCase.clear()
    }
}