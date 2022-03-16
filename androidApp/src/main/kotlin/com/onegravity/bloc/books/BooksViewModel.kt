package com.onegravity.bloc.books

import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.sample.books.*

class BooksViewModel(context: ActivityBlocContext) : BaseViewModel(context), BooksUseCase {

//    private val useCase: BooksUseCase = BooksUseCaseImplRedux(viewModelContext)
    private val useCase: BooksUseCase = BooksUseCaseImpl(viewModelContext, BooksRepositoryImpl())
//    private val useCase: BooksUseCase = BooksUseCaseImplSimple(viewModelContext, BooksRepositoryImpl())

    override val state
        get() = useCase.state

    override fun load() {
        useCase.load()
    }

    override fun clear() {
        useCase.clear()
    }
}