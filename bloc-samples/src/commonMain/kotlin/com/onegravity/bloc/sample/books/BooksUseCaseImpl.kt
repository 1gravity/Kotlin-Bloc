package com.onegravity.bloc.sample.books

import com.onegravity.bloc.bloc
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.toObservable

/**
 * Implements the BooksUseCase with two Blocs to demonstrate shared BlocState
 */
class BooksUseCaseImpl(
    context: BlocContext,
    private val repository: BooksRepository,
) : BooksUseCase {

    private val commonState = blocState<BookState>(BookState.Empty)

    private val clearBloc = bloc<BookState, BookAction.Clear>(context, commonState) {
        reduce {
            if (state is BookState.Loaded) BookState.Empty else state
        }
    }

    private val loadBloc = bloc<BookState, BookAction>(context, commonState) {
        thunk<BookAction.Load> {
            dispatch(BookAction.Loading)
            val nextAction = repository.loadBooks().toAction()
            dispatch(nextAction)
        }

        reduce<BookAction.Loading> { BookState.Loading }
        reduce<BookAction.LoadComplete> { action.result.toState() }
    }

    override fun load() {
        loadBloc.send(BookAction.Load)
    }

    override fun clear() {
        clearBloc.send(BookAction.Clear)
    }

    // There's no need to observe both Blocs because they share the same BlocState and only loadBloc
    // has side effects. We can merge multiple blocs sharing the same BlocState by doing:
    //    override val observable = listOf(loadBloc, clearBloc).toObservable()
    // or less generic (just two blocs):
    //    override val observable = loadBloc.toObservable(clearBloc)
    override val observable = loadBloc.toObservable()

}