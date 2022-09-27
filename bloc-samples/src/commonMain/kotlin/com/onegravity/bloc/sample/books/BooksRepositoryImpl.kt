package com.onegravity.bloc.sample.books

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.onegravity.bloc.sample.books.BooksRepository.Failure
import kotlinx.coroutines.delay
import kotlin.random.Random

private const val ERROR_THRESHOLD = 0.35
private const val NETWORK_ERROR_THRESHOLD = 0.5
private const val LOAD_DELAY = 1000L

class BooksRepositoryImpl : BooksRepository {

    override suspend fun loadBooks() =
        when (Random.nextFloat() < ERROR_THRESHOLD) {
            true -> {
                val network = Random.nextFloat() < NETWORK_ERROR_THRESHOLD
                val error = if (network) Failure.Network else Failure.Generic
                Err(error)
            }
            else -> Ok(books)
        }.also { delay(LOAD_DELAY) }

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

}
