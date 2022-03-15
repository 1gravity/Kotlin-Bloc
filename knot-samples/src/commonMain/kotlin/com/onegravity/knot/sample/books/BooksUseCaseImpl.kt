package com.onegravity.knot.sample.books

import com.onegravity.bloc.Stream
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.knot.state.blocState
import kotlinx.coroutines.delay
import com.onegravity.knot.sample.books.BooksRepository.*

/**
 * Implements the BooksUseCase with two [Bloc]s to demonstrate shared [BlocState]
 */
class BooksUseCaseImpl(
    context: BlocContext,
    private val repository: BooksRepository,
) : BooksUseCase {

    private val commonState = blocState<BookState>(BookState.Empty)

    private val clearBloc = bloc<BookState, BookEvent.Clear>(context, commonState) {
        reduce { state, _ ->
            when (state) {
                BookState.Empty -> state
                is BookState.Loaded -> BookState.Empty
                else -> state
            }
        }
    }

    private val loadBloc = bloc<BookState, BookEvent, BookState>(context, commonState) {
        thunkMatching<BookEvent.Load> { _, action, dispatch ->
            dispatch(BookEvent.Loading)
            delay(1000)
            val nextAction = repository.loadBooks().toAction()
            dispatch(nextAction)
        }

        reduce { state, action ->
            when (action) {
                BookEvent.Loading -> BookState.Loading
                is BookEvent.LoadComplete -> action.result.toState()
                else -> state
            }
        }
    }

    override val state: Stream<BookState>
        get() = commonState

    override fun load() {
        loadBloc.emit(BookEvent.Load)
    }

    override fun clear() {
        clearBloc.emit(BookEvent.Clear)
    }

}