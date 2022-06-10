package com.onegravity.bloc.sample.books

import com.github.michaelbull.result.Ok
import org.reduxkotlin.Store
import org.reduxkotlin.applyMiddleware
import org.reduxkotlin.createThreadSafeStore
import org.reduxkotlin.createThunkMiddleware

object BookStore {
    // Action (Bloc / Thunk -> Redux Store)
    sealed class ReduxAction {
        object Clear : ReduxAction()
        object Loading : ReduxAction()
        class Loaded(val books: BookResult) : ReduxAction()
    }

    // The Redux model / state
    data class ReduxModel(
        val isLoading: Boolean = false,
        val books: BookResult
    )

    // The reducer
    private fun reducer(state: ReduxModel, action: Any) = when (action) {
        is ReduxAction.Clear -> state.copy(isLoading = false, books = Ok(emptyList()))
        is ReduxAction.Loading -> state.copy(isLoading = true)
        is ReduxAction.Loaded -> state.copy(isLoading = false, books = action.books)
        else -> state
    }

    // The actual store
    internal val reduxStore: Store<ReduxModel> = createThreadSafeStore(
        reducer = ::reducer,
        preloadedState = ReduxModel(false, Ok(emptyList())),
        enhancer = applyMiddleware(createThunkMiddleware())
    )
}
