package com.onegravity.bloc.sample.books

import com.github.michaelbull.result.Ok
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.reduxkotlin.*

/* State / Redux Model */

data class ReduxModel(
    val isLoading: Boolean = false,
    val books: BookResult
)

/* Proposal (BLoC -> Redux Store) */

object ReduxProposal {
    // Load is a Thunk
    fun Load(coroutineScope: CoroutineScope, repository: BooksRepository) =
        repository.loadBooks(coroutineScope)
    object Clear
}

/* Action (Thunk -> Redux Store) */

sealed class ReduxAction {
    object Loading: ReduxAction()
    class Loaded(val books: BookResult) : ReduxAction()
}

/* Load Books Thunk */

private fun BooksRepository.loadBooks(coroutineScope: CoroutineScope): Thunk<ReduxModel> =
    { dispatch, _, _ ->
        coroutineScope.launch {
            dispatch(ReduxAction.Loading)
            dispatch(ReduxAction.Loaded(loadBooks()))
        }
    }

/* Reducer */

private fun reducer(state: ReduxModel, action: Any) = when (action) {
    is ReduxProposal.Clear -> state.copy(isLoading = false, books = Ok(emptyList()))
    is ReduxAction.Loading -> state.copy(isLoading = true)
    is ReduxAction.Loaded -> state.copy(isLoading = false, books = action.books)
    else -> state
}

internal val reduxStore: Store<ReduxModel> = createThreadSafeStore(
    reducer = ::reducer,
    preloadedState = ReduxModel(false, Ok(emptyList())),
    enhancer = applyMiddleware(createThunkMiddleware())
)
