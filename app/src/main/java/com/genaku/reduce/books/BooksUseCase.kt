package com.genaku.reduce.books

import com.genaku.reduce.CoroutineKnotState
import com.genaku.reduce.coroutineKnot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class BooksUseCase(private val repository: IBooksRepository) : IBooksUseCase {

    private val commonState = CoroutineKnotState<BooksState>(BooksState.Empty)

    private val clearKnot =
        coroutineKnot<BooksState, ClearBookChange, ClearBooksAction> {

            knotState = commonState

            changes { change ->
                when (change) {
                    ClearBookChange.Clear -> when (this) {
                        is BooksState.Content -> BooksState.Empty.only
                        is BooksState.Empty -> only
                        else -> unexpected(change)
                    }
                }
            }
        }

    private val loadKnot = coroutineKnot<BooksState, BooksChange, BooksAction> {

        knotState = commonState

        changes { change ->
            when (change) {
                BooksChange.Load -> when (this) {
                    BooksState.Empty,
                    is BooksState.Content,
                    is BooksState.Error -> BooksState.Loading + BooksAction.Load
                    else -> only
                }

                is BooksChange.Load.Success -> when (this) {
                    BooksState.Loading -> BooksState.Content(change.books).only
                    else -> unexpected(change)
                }

                is BooksChange.Load.Failure -> when (this) {
                    BooksState.Loading -> BooksState.Error(change.message).only
                    else -> unexpected(change)
                }
            }
        }

        suspendActions { action ->
            when (action) {
                BooksAction.Load -> {
                    delay(1000)
                    repository.loadBooks().toChange()
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
        loadKnot.offerChange(BooksChange.Load)
    }

    override fun clear() {
        clearKnot.offerChange(ClearBookChange.Clear)
    }

    private fun IBooksRepository.LoadBooksResult.toChange() =
        when (this) {
            is IBooksRepository.LoadBooksResult.Success ->
                BooksChange.Load.Success(books)
            is IBooksRepository.LoadBooksResult.Failure.Network ->
                BooksChange.Load.Failure("Network error. Check Internet connection and try again.")
            IBooksRepository.LoadBooksResult.Failure.Generic ->
                BooksChange.Load.Failure("Generic error, please try again.")
        }
}