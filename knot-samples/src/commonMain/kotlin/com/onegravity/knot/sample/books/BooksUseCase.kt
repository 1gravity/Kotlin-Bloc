package com.onegravity.knot.sample.books

import co.touchlab.kermit.Logger
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.sample.books.IBooksRepository.*
import com.onegravity.knot.knot
import com.onegravity.knot.knotState
import com.onegravity.knot.sample.booksRedux.IBooksUseCase
import com.onegravity.knot.state.KnotState
import kotlinx.coroutines.delay

class BooksUseCase(
    context: KnotContext,
    private val repository: IBooksRepository,
) : IBooksUseCase {

    private val commonState = knotState<BooksState>(BooksState.Empty)

    /**
     * This is just an example how to "combine" two Knots.
     */
    data class DemoSideEffect(val s: String)
    private val clearKnotPassThrough = knot<BooksState, BooksState, BooksState, DemoSideEffect>(context, commonState) {
        reduce { _, proposal ->
            proposal + DemoSideEffect("$proposal")
        }
        execute {
            Logger.withTag("knot").i("Proposal was: ${it.s}")
            null
        }
    }
    // This shows that the created [Knot] is also a [KnotState]
    private val clearKnotState: KnotState<BooksState, BooksState> = clearKnotPassThrough

    private val clearKnot = knot<BooksState, ClearBookEvent>(context, clearKnotState) {
        reduce { state, event ->
            when (event) {
                ClearBookEvent.Clear -> when (state) {
                    is BooksState.Content -> BooksState.Empty.toEffect()
                    is BooksState.Empty -> state.toEffect()
                    else -> state.unexpected(event)
                }
            }
        }
    }

    private val loadKnot = knot<BooksState, BooksEvent, BooksState, BooksSideEffect>(context, commonState) {
        reduce { state, event ->
            when (event) {
                BooksEvent.Load -> when (state) {
                    BooksState.Empty,
                    is BooksState.Content,
                    is BooksState.BooksError -> BooksState.Loading + BooksSideEffect.Load
                    else -> state.toEffect()
                }
                is BooksEvent.Success -> when (state) {
                    BooksState.Loading -> BooksState.Content(event.books).toEffect()
                    else -> state.unexpected(event)
                }
                is BooksEvent.Failure ->  when (state) {
                    BooksState.Loading -> BooksState.BooksError(event.message).toEffect()
                    else -> state.unexpected(event)
                }
            }
        }

        execute { sideEffect ->
            when (sideEffect) {
                BooksSideEffect.Load -> {
                    delay(1000)
                    repository.loadBooks().toEvent()
                }
            }
        }
    }

    override val state = commonState

    override fun load() {
        loadKnot.emit(BooksEvent.Load)
    }

    override fun clear() {
        clearKnot.emit(ClearBookEvent.Clear)
    }

    private fun LoadBooksResult.toEvent() =
        when (this) {
            is LoadBooksResult.Success -> BooksEvent.Success(books)
            is LoadBooksResult.Failure.Network -> BooksEvent.Failure("Network error. Check Internet connection and try again.")
            LoadBooksResult.Failure.Generic -> BooksEvent.Failure("Generic error, please try again.")
        }
}