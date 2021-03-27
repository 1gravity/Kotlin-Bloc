package com.genaku.reduce

sealed class State {
    object Empty : State()
    object Loading : State()
    data class Content(val books: List<Book>) : State()
    data class Error(val message: String) : State()
}

data class Book(val title: String, val year: String)

sealed class Action {
    object Load : Action()
}

sealed class Change {
    object Load : Change() {
        data class Success(val books: List<Book>) : Change()
        data class Failure(val message: String) : Change()
    }

    object Clear : Change()
}
