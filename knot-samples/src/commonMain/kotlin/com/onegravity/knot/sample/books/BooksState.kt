package com.onegravity.knot.sample.books

data class Book(val title: String, val year: String)

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
    class LoadComplete(val result: BookResult) : BookEvent()
}
