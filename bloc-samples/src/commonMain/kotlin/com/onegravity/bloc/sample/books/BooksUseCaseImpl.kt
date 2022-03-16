package com.onegravity.bloc.sample.books

import com.onegravity.bloc.Stream
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.sample.books.BooksRepository.*

/**
 * Implements the BooksUseCase with two [Bloc]s to demonstrate shared [BlocState]
 */
class BooksUseCaseImpl(
    context: BlocContext,
    private val repository: BooksRepository,
) : BooksUseCase {

    private val commonState = blocState<BookState>(BookState.Empty)

    private val clearBloc = bloc<BookState, BookAction.Clear>(context, commonState) {
        reduce { state, _ ->
            if (state is BookState.Loaded) BookState.Empty else state
        }
    }

    private val loadBloc = bloc<BookState, BookAction, BookState>(context, commonState) {
        thunkMatching<BookAction.Load> { _, _, dispatch ->
            dispatch(BookAction.Loading)
            val nextAction = repository.loadBooks().toAction()
            dispatch(nextAction)
        }

        reduce { state, action ->
            when (action) {
                BookAction.Loading -> BookState.Loading
                is BookAction.LoadComplete -> action.result.toState()
                else -> state
            }
        }
    }

    override val state: Stream<BookState>
        get() = commonState

    override fun load() {
        loadBloc.emit(BookAction.Load)
    }

    override fun clear() {
        clearBloc.emit(BookAction.Clear)
    }

}