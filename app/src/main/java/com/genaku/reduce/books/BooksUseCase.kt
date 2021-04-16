package com.genaku.reduce.books

import com.genaku.reduce.CoroutineKnotState
import com.genaku.reduce.knot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class BooksUseCase(private val repository: IBooksRepository) : IBooksUseCase {

    private val commonState = CoroutineKnotState<BooksState>(BooksState.Empty)

    private val clearKnot =
        knot<BooksState, ClearBookIntent, ClearBooksAction> {

            knotState = commonState

            reduce { intent ->
                when (intent) {
                    ClearBookIntent.Clear -> when (this) {
                        is BooksState.Content -> BooksState.Empty.stateOnly
                        is BooksState.Empty -> stateOnly
                        else -> unexpected(intent)
                    }
                }
            }
        }

    private val loadKnot = knot<BooksState, BooksIntent, BooksAction> {

        knotState = commonState

        reduce { intent ->
            when (intent) {
                BooksIntent.Load -> when (this) {
                    BooksState.Empty,
                    is BooksState.Content,
                    is BooksState.BooksError -> BooksState.Loading + BooksAction.Load
                    else -> stateOnly
                }
                is BooksIntent.Success -> when (this) {
                    BooksState.Loading -> BooksState.Content(intent.books).stateOnly
                    else -> unexpected(intent)
                }
                is BooksIntent.Failure ->  when (this) {
                    BooksState.Loading -> BooksState.BooksError(intent.message).stateOnly
                    else -> unexpected(intent)
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