package com.onegravity.bloc.sample.books

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import kotlin.random.Random
import com.onegravity.bloc.sample.books.BooksRepository.*

class BooksRepositoryImpl : BooksRepository {

    override fun loadBooks() =
        when (Random.nextFloat() < 0.35) {
            true -> {
                val network = Random.nextFloat() < 0.5
                val error = if (network) Failure.Network else Failure.Generic
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

}