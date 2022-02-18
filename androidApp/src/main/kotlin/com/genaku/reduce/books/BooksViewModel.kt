package com.genaku.reduce.books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class BooksViewModel(useCase: IBooksUseCase = BooksUseCase(BooksRepository())) : ViewModel(),
    IBooksUseCase by useCase {

    init {
        start(viewModelScope)
    }
}