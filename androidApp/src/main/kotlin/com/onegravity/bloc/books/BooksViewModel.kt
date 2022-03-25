package com.onegravity.bloc.books

import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.sample.books.*
import com.onegravity.bloc.utils.BlocObservable
import com.onegravity.bloc.utils.BlocObservableOwner

class BooksViewModel(context: ActivityBlocContext) :
    BaseViewModel(context),
    BlocObservableOwner<BookState, Unit>,
    BooksUseCase {

    private val useCase: BooksUseCase = BooksUseCaseImplRedux(viewModelContext, BooksRepositoryImpl())
//    private val useCase: BooksUseCase = BooksUseCaseImpl(viewModelContext, BooksRepositoryImpl())
//    private val useCase: BooksUseCase = BooksUseCaseImplSimple(viewModelContext, BooksRepositoryImpl())

    override val observable = useCase.observable

    override fun load() {
        useCase.load()
    }

    override fun clear() {
        useCase.clear()
    }

}