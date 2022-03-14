package com.onegravity.knot.sample.books

import com.github.michaelbull.result.mapBoth
import com.onegravity.knot.*
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.state.knotState
import kotlinx.coroutines.delay
import com.onegravity.knot.sample.books.BooksRepository.*

/**
 * Implements the BooksUseCase with a single Knot and also uses [SideEffect]s
 */
class BooksUseCaseImplSimple(
    context: KnotContext,
    private val repository: BooksRepository,
) : BooksUseCase {

    private val knot = knotSimple2<BookState, BookEvent>(context, knotState(BookState.Empty)) {
        reduce { _, action, dispatch ->
            when (action) {
                BookEvent.Clear -> dispatch(BookState.Empty)
                BookEvent.Load -> {
                    dispatch(BookState.Loading)
                    delay(1000)
                    val state = repository.loadBooks().toState()
                    dispatch(state)
                }
                else -> { }
            }
        }
    }

    private fun BookResult.toState() =
        mapBoth(
            { books -> if (books.isEmpty()) BookState.Empty else BookState.Loaded(books) },
            { failure ->
                val message = when (failure) {
                    is Failure.Network -> "Network error. Check Internet connection and try again."
                    is Failure.Generic -> "Generic error, please try again."
                }
                BookState.Failure(message)
            })

    override val state: Stream<BookState>
        get() = knot

    override fun load() {
        knot.emit(BookEvent.Load)
    }

    override fun clear() {
        knot.emit(BookEvent.Clear)
    }

}