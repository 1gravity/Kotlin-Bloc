package com.onegravity.bloc.sample.books

import com.github.michaelbull.result.Result

typealias BookResult = Result<List<Book>, BooksRepository.Failure>

interface BooksRepository {

    fun loadBooks(): BookResult

    sealed class Failure {
        object Network : Failure()
        object Generic : Failure()
    }
}