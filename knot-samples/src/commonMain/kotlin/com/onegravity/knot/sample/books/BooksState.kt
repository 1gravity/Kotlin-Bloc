package com.onegravity.knot.sample.books

sealed class BooksState {
    object Empty : BooksState()
    object Loading : BooksState()
    data class Content(val books: List<Book>) : BooksState()
    data class BooksError(val message: String) : BooksState()
}

data class Book(val title: String, val year: String)

sealed class BooksSideEffect {
    object Load : BooksSideEffect()
}

sealed class BooksEvent {
    object Load : BooksEvent()
    class Success(val books: List<Book>) : BooksEvent()
    class Failure(val message: String) : BooksEvent()
}

sealed class ClearBookEvent {
    object Clear : ClearBookEvent()
}
