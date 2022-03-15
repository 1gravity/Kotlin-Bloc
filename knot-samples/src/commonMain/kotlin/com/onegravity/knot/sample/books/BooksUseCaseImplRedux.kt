package com.onegravity.knot.sample.books

import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.Stream
import com.onegravity.bloc.bloc
import com.onegravity.knot.*
import com.onegravity.bloc.context.BlocContext
import com.onegravity.knot.state.ReduxKnotState

/**
 * Implements the BooksUseCase with a single Knot and a [ReduxKnotState].
 */
class BooksUseCaseImplRedux(context: BlocContext) : BooksUseCase {
    private val mapModel2State: Mapper<Books, BookState> = { books ->
        books.mapBoth(
            { if (it.isEmpty()) BookState.Empty else BookState.Loaded(it) },
            {
                val message = when (it) {
                    is ReduxFailure.Network -> "Network error. Check Internet connection and try again."
                    is ReduxFailure.Generic -> "Generic error, please try again."
                }
                BookState.Failure(message)
            }
        )
    }

    private val knot = bloc<BookState, BookEvent, Action>(
        context,
        reduxStore.toKnotState(context, BookState.Empty, { it }, mapModel2State)
    ) {
        reduce { _, event ->
            when (event) {
                BookEvent.Load -> Action.Load
                else -> Action.Clear
            }
        }
    }

    override val state: Stream<BookState>
        get() = knot

    override fun load() {
        knot.emit(BookEvent.Load)
    }

    override fun clear() {
        knot.emit(BookEvent.Clear)
    }

}