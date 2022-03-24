package com.onegravity.bloc.sample.books

import com.onegravity.bloc.Bloc
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.sample.books.BooksRepository.*
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.utils.toObservable

/**
 * Implements the BooksUseCase with two [Bloc]s to demonstrate shared [BlocState]
 */
class BooksUseCaseImpl(
    context: BlocContext,
    private val repository: BooksRepository,
) : BooksUseCase {

    private val commonState = blocState<BookState>(BookState.Empty)

    private val clearBloc = bloc<BookState, BookAction.Clear>(context, commonState) {
        state {
            if (state is BookState.Loaded) BookState.Empty else state
        }
    }

    private val loadBloc = bloc<BookState, BookAction, BookState>(context, commonState) {
        thunk<BookAction.Load> {
            dispatch(BookAction.Loading)
            val nextAction = repository.loadBooks().toAction()
            dispatch(nextAction)
        }

        state<BookAction.Loading> {
            BookState.Loading
        }

        state<BookAction.LoadComplete> { action.result.toState() }
    }

    override fun load() {
        loadBloc.send(BookAction.Load)
    }

    override fun clear() {
        clearBloc.send(BookAction.Clear)
    }

    // there's no need to observe both Blocs because they share the same BlocState and only loadBloc
    // has side effects
    override val observable = loadBloc.toObservable()

    // todo create operator to merge multiple Blocs into one BlocObservable
    //      (if they share the same BlocState)

}