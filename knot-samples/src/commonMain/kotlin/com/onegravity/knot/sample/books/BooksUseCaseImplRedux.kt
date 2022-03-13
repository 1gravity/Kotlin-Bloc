package com.onegravity.knot.sample.books

import com.github.michaelbull.result.mapBoth
import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.state.ReduxKnotState

/**
 * Implements the BooksUseCase with a single Knot and a [ReduxKnotState].
 */
class BooksUseCaseImplRedux(context: KnotContext) : BooksUseCase {
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

    private val knot = knot<BookState, BookEvent, Action, Nothing>(
        context,
        reduxStore.toKnotState(context, BookState.Empty, { it }, mapModel2State)
    ) {
        reduce { _, event ->
            when (event) {
                BookEvent.Load -> Effect(Action.Load)
                else -> Effect(Action.Clear)
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