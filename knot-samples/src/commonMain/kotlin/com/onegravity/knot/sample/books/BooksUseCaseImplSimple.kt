package com.onegravity.knot.sample.books

import com.onegravity.bloc.Stream
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.logger
import kotlinx.coroutines.delay

/**
 * Implements the BooksUseCase with a single [Bloc] and [Thunk]s
 */
class BooksUseCaseImplSimple(
    context: BlocContext,
    private val repository: BooksRepository,
) : BooksUseCase {

    private val bloc = bloc<BookState, BookEvent>(context, BookState.Empty) {
        thunk<BookEvent.Load> { _, dispatch ->
            logger.i("Thunk 1")
            dispatch(BookEvent.Loading)
            delay(1000)
            val nextAction = repository.loadBooks().toAction()
            dispatch(nextAction)
        }

        thunk<BookEvent.Load> { _, dispatch ->
            logger.i("Thunk 2")
            dispatch(BookEvent.Loading)
            delay(1000)
            val nextAction = repository.loadBooks().toAction()
            dispatch(nextAction)
        }

        thunk { _, action, dispatch ->
            logger.i("Thunk 3: $action")
            if (action == BookEvent.Load) {
                dispatch(BookEvent.Loading)
                delay(1000)
                val nextAction = repository.loadBooks().toAction()
                dispatch(nextAction)
            } else {
                dispatch(action)
            }
        }

        reduce { state, action ->
            logger.i("Reduce: $action")
            when (action) {
                BookEvent.Clear -> BookState.Empty
                BookEvent.Loading -> BookState.Loading
                is BookEvent.LoadComplete -> action.result.toState()
                else -> state
            }
        }
    }

    override val state: Stream<BookState>
        get() = bloc

    override fun load() {
        bloc.emit(BookEvent.Load)
    }

    override fun clear() {
        bloc.emit(BookEvent.Clear)
    }

}
