package com.onegravity.knot.sample.books

import com.github.michaelbull.result.mapBoth

data class Book(val title: String, val year: String)

fun BookResult.toState(): BookState =
    mapBoth(
        { books -> if (books.isEmpty()) BookState.Empty else BookState.Loaded(books) },
        { failure ->
            val message = when (failure) {
                is BooksRepository.Failure.Network -> "Network error. Check Internet connection and try again."
                else -> "Generic error, please try again."
            }
            BookState.Failure(message)
        })

fun BookResult.toAction(): BookEvent = BookEvent.LoadComplete(this)

sealed class BookState {
    object Empty : BookState()
    object Loading : BookState()
    data class Loaded(val books: List<Book>) : BookState()
    data class Failure(val message: String) : BookState()
}

sealed class BookSideEffect {
    object Load : BookSideEffect()
}

sealed class BookEvent {
    object Load : BookEvent()
    object Clear : BookEvent()
    object Loading : BookEvent()
    class LoadComplete(val result: BookResult) : BookEvent()
}
