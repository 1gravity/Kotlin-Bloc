package com.onegravity.knot.sample.books

import com.github.michaelbull.result.Result

typealias BookResult = Result<List<Book>, IBooksRepository.Failure>

interface IBooksRepository {

    fun loadBooks(): BookResult

    sealed class Failure {
        object Network : Failure()
        object Generic : Failure()
    }
}