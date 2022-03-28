package com.onegravity.bloc.books

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.blocContext
import com.onegravity.bloc.sample.books.BookState
import com.onegravity.bloc.sample.books.BooksRepositoryImpl
import com.onegravity.bloc.sample.books.BooksUseCase
import com.onegravity.bloc.sample.books.BooksUseCaseImplRedux
import com.onegravity.bloc.utils.BlocObservableOwner

class BooksViewModel : ViewModel(), BlocObservableOwner<BookState, Unit>, BooksUseCase {

    private val useCase: BooksUseCase = BooksUseCaseImplRedux(blocContext(), BooksRepositoryImpl())
//    private val useCase: BooksUseCase = BooksUseCaseImpl(blocContext(), BooksRepositoryImpl())
//    private val useCase: BooksUseCase = BooksUseCaseImplSimple(blocContext(), BooksRepositoryImpl())

    override val observable = useCase.observable

    override fun load() {
        useCase.load()
    }

    override fun clear() {
        useCase.clear()
    }

}