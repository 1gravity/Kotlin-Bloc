package com.genaku.reduce.books

import com.genaku.reduce.StateAction
import com.genaku.reduce.StateIntent
import com.genaku.reduce.State

sealed class BooksState : State {
    object Empty : BooksState()
    object Loading : BooksState()
    data class Content(val books: List<Book>) : BooksState()
    data class BooksError(val message: String) : BooksState()
}

data class Book(val title: String, val year: String)

sealed class BooksAction : StateAction {
    object Load : BooksAction()
}

sealed class BooksIntent : StateIntent {
    object Load : BooksIntent()
    class Success(val books: List<Book>) : BooksIntent()
    class Failure(val message: String) : BooksIntent()
}

sealed class ClearBookIntent : StateIntent {
    object Clear : ClearBookIntent()
}

class ClearBooksAction : StateAction