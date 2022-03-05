package com.genaku.reduce.books

import com.onegravity.knot.*
import com.onegravity.knot.state.CoroutineKnotState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class BooksUseCase(private val repository: IBooksRepository) : IBooksUseCase {

    private val commonState = CoroutineKnotState<BooksState>(BooksState.Empty)

    private val clearKnot =
        knot<BooksState, ClearBookIntent, Nothing> {

            knotState = commonState

            reduce { scope, state, intent ->
                when (intent) {
                    ClearBookIntent.Clear -> when (state) {
                        is BooksState.Content -> BooksState.Empty.asEffect
                        is BooksState.Empty -> state.toEffect
                        else -> state.unexpected(intent)
                    }
                }
            }
        }

    private val loadKnot = knot<BooksState, BooksIntent, BooksAction> {

        knotState = commonState

        reduce { state, intent ->
            when (intent) {
                BooksIntent.Load -> when (state) {
                    BooksState.Empty,
                    is BooksState.Content,
                    is BooksState.BooksError -> BooksState.Loading + BooksAction.Load
                    else -> state.toEffect
                }
                is BooksIntent.Success -> when (state) {
                    BooksState.Loading -> BooksState.Content(intent.books).asEffect
                    else -> state.unexpected(intent)
                }
                is BooksIntent.Failure ->  when (state) {
                    BooksState.Loading -> BooksState.BooksError(intent.message).asEffect
                    else -> state.unexpected(intent)
                }
            }
        }

        suspendActions { action ->
            when (action) {
                BooksAction.Load -> {
                    delay(1000)
                    repository.loadBooks().toIntent()
                }
            }
        }
    }

    override val state: StateFlow<BooksState>
        get() = commonState.state

    override fun start(coroutineScope: CoroutineScope) {
        loadKnot.start(coroutineScope)
        clearKnot.start(coroutineScope)
    }

    override fun load() {
        loadKnot.offerIntent(BooksIntent.Load)
    }

    override fun clear() {
        clearKnot.offerIntent(ClearBookIntent.Clear)
    }

    private fun IBooksRepository.LoadBooksResult.toIntent() =
        when (this) {
            is IBooksRepository.LoadBooksResult.Success ->
                BooksIntent.Success(books)
            is IBooksRepository.LoadBooksResult.Failure.Network ->
                BooksIntent.Failure("Network error. Check Internet connection and try again.")
            IBooksRepository.LoadBooksResult.Failure.Generic ->
                BooksIntent.Failure("Generic error, please try again.")
        }
}