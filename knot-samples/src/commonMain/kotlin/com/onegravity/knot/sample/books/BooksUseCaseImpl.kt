package com.onegravity.knot.sample.books

import com.github.michaelbull.result.mapBoth
import com.onegravity.knot.Stream
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.knot
import com.onegravity.knot.knotState
import kotlinx.coroutines.delay
import com.onegravity.knot.sample.books.IBooksRepository.*

/**
 * Implements the IBooksUseCase with two [Knot]s to demonstrate shared [KnotState]
 */
class BooksUseCaseImpl(
    context: KnotContext,
    private val repository: IBooksRepository,
) : BooksUseCase {

    private val commonState = knotState<BookState>(BookState.Empty)

    private val clearKnot = knot<BookState, BookEvent>(context, commonState) {
        reduce { state, event ->
            when (event) {
                BookEvent.Clear -> when (state) {
                    BookState.Empty -> state.toEffect()
                    is BookState.Loaded -> BookState.Empty.toEffect()
                    else -> state.unexpected(event)
                }
                else -> state.toEffect()
            }
        }
    }

    private val loadKnot = knot<BookState, BookEvent, BookState, BookSideEffect>(context, commonState) {
        reduce { state, event ->
            when (event) {
                BookEvent.Load -> when (state) {
                    BookState.Empty, is BookState.Loaded, is BookState.Failure ->
                        BookState.Loading + BookSideEffect.Load
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
                else -> state.toEffect()
            }
        }

        execute {
            delay(1000)
            val result = repository.loadBooks()
            BookEvent.LoadComplete(result)
        }
    }

    override val state: Stream<BookState>
        get() = commonState

    override fun load() {
        loadKnot.emit(BookEvent.Load)
    }

    override fun clear() {
        clearKnot.emit(BookEvent.Clear)
    }

}