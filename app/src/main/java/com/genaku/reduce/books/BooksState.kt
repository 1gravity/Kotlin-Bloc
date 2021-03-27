package com.genaku.reduce.books

sealed class BooksState {
    object Empty : BooksState()
    object Loading : BooksState()
    data class Content(val books: List<Book>) : BooksState()
    data class Error(val message: String) : BooksState()
}

data class Book(val title: String, val year: String)

sealed class BooksAction {
    object Load : BooksAction()
}

sealed class BooksChange {
    object Load : BooksChange() {
        data class Success(val books: List<Book>) : BooksChange()
        data class Failure(val message: String) : BooksChange()
    }
}

sealed class ClearBookChange {
    object Clear : ClearBookChange()
}

class ClearBooksAction