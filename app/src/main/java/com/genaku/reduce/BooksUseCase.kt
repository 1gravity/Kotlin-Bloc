package com.genaku.reduce

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

class BooksUseCase(private val repository: IBooksRepository) : IBooksUseCase {

    private val knot = coroutineKnot<State, Change, Action> {

        initialState = State.Empty

        changes { change ->
            when (change) {
                Change.Load -> when (this) {
                    State.Empty,
                    is State.Content,
                    is State.Error -> State.Loading + Action.Load
                    else -> only
                }

                is Change.Load.Success -> when (this) {
                    State.Loading -> State.Content(change.books).only
                    else -> unexpected(change)
                }

                is Change.Load.Failure -> when (this) {
                    State.Loading -> State.Error(change.message).only
                    else -> unexpected(change)
                }

                Change.Clear -> when (this) {
                    is State.Content -> State.Empty.only
                    is State.Empty -> only
                    else -> unexpected(change)
                }
            }
        }

        suspendActions { action ->
            when (action) {
                Action.Load -> {
                    delay(2000)
                    repository.loadBooks().toChange()
                }
            }
        }
    }

    override val state: StateFlow<State>
        get() = knot.state

    override fun start(coroutineScope: CoroutineScope) {
        knot.start(coroutineScope)
    }

    override fun load() {
        knot.offerChange(Change.Load)
    }

    override fun clear() {
        knot.offerChange(Change.Clear)
    }

    private fun IBooksRepository.LoadBooksResult.toChange() =
        when (this) {
            is IBooksRepository.LoadBooksResult.Success ->
                Change.Load.Success(books)
            is IBooksRepository.LoadBooksResult.Failure.Network ->
                Change.Load.Failure("Network error. Check Internet connection and try again.")
            IBooksRepository.LoadBooksResult.Failure.Generic ->
                Change.Load.Failure("Generic error, please try again.")
        }
}