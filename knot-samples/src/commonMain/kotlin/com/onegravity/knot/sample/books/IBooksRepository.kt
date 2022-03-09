package com.onegravity.knot.sample.books

interface IBooksRepository {

    fun loadBooks(): LoadBooksResult

    sealed class LoadBooksResult {
        data class Success(val books: List<Book>) : LoadBooksResult()
        sealed class Failure : LoadBooksResult() {
            object Network : Failure()
            object Generic : Failure()
        }
    }
}