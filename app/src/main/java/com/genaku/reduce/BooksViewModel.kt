package com.genaku.reduce

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class BooksViewModel(useCase: IBooksUseCase = BooksUseCase(BooksRepository())) : ViewModel(),
    IBooksUseCase by useCase {

    init {
        start(viewModelScope)
    }
}