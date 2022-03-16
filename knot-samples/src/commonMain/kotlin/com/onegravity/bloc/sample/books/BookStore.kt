package com.onegravity.bloc.sample.books

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.reduxkotlin.Store
import org.reduxkotlin.createThreadSafeStore
import kotlin.random.Random

sealed class Action {
    object Load: Action()
    object Clear: Action()
}

sealed class ReduxFailure {
    object Network : ReduxFailure()
    object Generic : ReduxFailure()
}

typealias Books = Result<List<Book>, ReduxFailure>

sealed class State {
    object Empty : State()
    object Loading : State()
    class Loaded(books: Books) : State()
}

private fun reducer(state: Books, action: Any) = when (action) {
    is Action.Load -> loadBooks()
    is Action.Clear -> Ok(emptyList())
    else -> state
}

internal val reduxStore: Store<Books> = createThreadSafeStore(::reducer, Ok(emptyList()))

private fun loadBooks() =
    when (Random.nextFloat() < 0.35) {
        true -> {
            val network = Random.nextFloat() < 0.5
            val error = if (network) ReduxFailure.Network else ReduxFailure.Generic
            Err(error)
        }
        else -> Ok(books)
    }

private val books = listOf(
    Book("The Hobbit or There and Back Again", "1937"),
    Book("Leaf by Niggle", "1945"),
    Book("The Lay of Aotrou and Itroun", "1945"),
    Book("Farmer Giles of Ham", "1949"),
    Book("The Homecoming of Beorhtnoth Beorhthelm's Son", "1953"),
    Book("The Lord of the Rings - The Fellowship of the Ring", "1954"),
    Book("The Lord of the Rings - The Two Towers", "1954"),
    Book("The Lord of the Rings - The Return of the King", "1955"),
    Book("The Adventures of Tom Bombadil", "1962"),
    Book("Tree and Leaf", "1964"),
    Book("The Tolkien Reader", "1966"),
    Book("The Road Goes Ever On", "1967"),
    Book("Smith of Wootton Major", "1967")
)
