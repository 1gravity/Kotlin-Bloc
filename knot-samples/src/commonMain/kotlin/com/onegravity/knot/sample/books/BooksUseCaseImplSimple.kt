package com.onegravity.knot.sample.books

import com.github.michaelbull.result.mapBoth
import com.onegravity.knot.SideEffect
import com.onegravity.knot.Stream
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.knot
import com.onegravity.knot.state.knotState
import kotlinx.coroutines.delay
import com.onegravity.knot.sample.books.IBooksRepository.*

/**
 * Implements the BooksUseCase with a single Knot and also uses [SideEffect]s
 */
class BooksUseCaseImplSimple(
    context: KnotContext,
    private val repository: IBooksRepository,
) : BooksUseCase {

    private val knot = knot<BookState, BookEvent>(context, knotState(BookState.Empty)) {
        reduce { state, event ->
            when (event) {
                BookEvent.Clear -> when (state) {
                    BookState.Empty -> state.toEffect()
                    is BookState.Loaded -> BookState.Empty.toEffect()
                    else -> state.unexpected(event)
                }
                BookEvent.Load -> when (state) {
                    BookState.Empty, is BookState.Loaded, is BookState.Failure ->
                        BookState.Loading + loadBooks()
                    else -> state.toEffect()
                }
                is BookEvent.LoadComplete -> event.result.mapBoth(
                    { books -> if (books.isEmpty()) BookState.Empty else BookState.Loaded(books) },
                    { failure ->
                        val message = when (failure) {
                            is Failure.Network -> "Network error. Check Internet connection and try again."
                            is Failure.Generic -> "Generic error, please try again."
                        }
                        BookState.Failure(message)
                    }
                ).toEffect()
            }
        }
    }

    private fun loadBooks() = SideEffect<BookEvent> {
        delay(1000)
        val result = repository.loadBooks()
        BookEvent.LoadComplete(result)
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