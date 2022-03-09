package com.genaku.reduce.books

import androidx.lifecycle.ViewModel
import com.onegravity.knot.context.KnotContext
import com.onegravity.knot.sample.books.BooksRepository
import com.onegravity.knot.sample.books.BooksUseCase
import com.onegravity.knot.sample.books.IBooksUseCase

class BooksViewModel(
    context: KnotContext,
    useCase: IBooksUseCase = BooksUseCase(context, BooksRepository())
) : ViewModel(), IBooksUseCase by useCase