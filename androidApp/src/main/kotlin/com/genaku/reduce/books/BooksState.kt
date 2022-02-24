package com.genaku.reduce.books

import com.onegravity.knot.State

sealed class BooksState : State {
    object Empty : BooksState()
    object Loading : BooksState()
    data class Content(val books: List<Book>) : BooksState()
    data class BooksError(val message: String) : BooksState()
}

data class Book(val title: String, val year: String)

sealed class BooksAction {
    object Load : BooksAction()
}

sealed class BooksIntent {
    object Load : BooksIntent()
    class Success(val books: List<Book>) : BooksIntent()
    class Failure(val message: String) : BooksIntent()
}

sealed class ClearBookIntent {
    object Clear : ClearBookIntent()
}

class ClearBooksAction