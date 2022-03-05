package com.genaku.reduce.books

import com.onegravity.knot.knot
import com.onegravity.knot.state.SimpleKnotState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class BooksUseCase(private val repository: IBooksRepository) : IBooksUseCase {

    private val commonState = SimpleKnotState<BooksState>(BooksState.Empty)

    private val clearKnot =
        knot<BooksState, ClearBookIntent, BooksState, Nothing> {

            knotState = commonState

            reduce { state, intent ->
                when (intent) {
                    ClearBookIntent.Clear -> when (state) {
                        is BooksState.Content -> BooksState.Empty.toEffect()
                        is BooksState.Empty -> state.toEffect()
                        else -> state.unexpected(intent)
                    }
                }
            }
        }

    private val loadKnot = knot<BooksState, BooksIntent, BooksState, BooksAction> {

        knotState = commonState

        reduce { state, intent ->
            when (intent) {
                BooksIntent.Load -> when (state) {
                    BooksState.Empty,
                    is BooksState.Content,
                    is BooksState.BooksError -> BooksState.Loading + BooksAction.Load
                    else -> state.toEffect()
                }
                is BooksIntent.Success -> when (state) {
                    BooksState.Loading -> BooksState.Content(intent.books).toEffect()
                    else -> state.unexpected(intent)
                }
                is BooksIntent.Failure ->  when (state) {
                    BooksState.Loading -> BooksState.BooksError(intent.message).toEffect()
                    else -> state.unexpected(intent)
                }
            }
        }

        execute { sideEffect ->
            when (sideEffect) {
                BooksAction.Load -> {
                    runBlocking {
                        delay(1000)
                        repository.loadBooks().toIntent()
                    }
                }
            }
        }
    }

    override val state = commonState

    override fun start(coroutineScope: CoroutineScope) {
        loadKnot.start(coroutineScope)
        clearKnot.start(coroutineScope)
    }

    override fun load() {
        loadKnot.emit(BooksIntent.Load)
    }

    override fun clear() {
        clearKnot.emit(ClearBookIntent.Clear)
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