//package com.onegravity.knot.sample.booksRedux
//
//import com.github.michaelbull.result.*
//import com.onegravity.knot.context.KnotContext
//import com.onegravity.knot.knot
//import com.onegravity.knot.knotState
//import com.onegravity.knot.sample.books.BooksState
//import com.onegravity.knot.sample.books.ClearBookEvent
//import com.onegravity.knot.sample.books.IBooksRepository
//import com.onegravity.knot.state.ReduxKnotState
//import kotlinx.coroutines.delay
//
//class BooksUseCase(
//    context: KnotContext,
//    private val repository: IBooksRepository,
//) : IBooksUseCase {
//
//    private val reduxStore = BookStore(repository).reduxStore
//
//    private val commonState = ReduxKnotState<BooksState, ClearBookEvent, Books, Books>(
//        context = context,
//        initialState = BooksState.Empty,
//        store = reduxStore,
//        selector = { state -> state },
//        acceptor = { acceptor},
//        mapper = { books ->
//            books.mapBoth(
//                { if (it.isEmpty()) BooksState.Empty else BooksState.Content(it) },
//                { BooksState.BooksError(it) }
//            )
//        }
//    )
///*
//sealed class BooksState {
//    object Empty : BooksState()
//    object Loading : BooksState()
//    data class Content(val books: List<Book>) : BooksState()
//    data class BooksError(val message: String) : BooksState()
//}
//
// */
//    private val clearKnot = knot<BooksState, ClearBookEvent, BooksState>(context) {
//        knotState = commonState
//        reduce { state, event ->
//            when (event) {
//                ClearBookEvent.Clear -> when (state) {
//                    is BooksState.Content -> BooksState.Empty.toEffect()
//                    is BooksState.Empty -> state.toEffect()
//                    else -> state.unexpected(event)
//                }
//            }
//        }
//    }
//
//    private val loadKnot = knot<BooksState, BooksEvent, BooksState, BooksSideEffect>(context) {
//        knotState = commonState
//        reduce { state, event ->
//            when (event) {
//                BooksEvent.Load -> when (state) {
//                    BooksState.Empty,
//                    is BooksState.Content,
//                    is BooksState.BooksError -> BooksState.Loading + BooksSideEffect.Load
//                    else -> state.toEffect()
//                }
//                is BooksEvent.Success -> when (state) {
//                    BooksState.Loading -> BooksState.Content(event.books).toEffect()
//                    else -> state.unexpected(event)
//                }
//                is BooksEvent.Failure ->  when (state) {
//                    BooksState.Loading -> BooksState.BooksError(event.message).toEffect()
//                    else -> state.unexpected(event)
//                }
//            }
//        }
//
//        execute { sideEffect ->
//            when (sideEffect) {
//                BooksSideEffect.Load -> {
//                    delay(1000)
//                    repository.loadBooks().toEvent()
//                }
//            }
//        }
//    }
//
//    override val state = commonState
//
//    override fun load() {
//        loadKnot.emit(BooksEvent.Load)
//    }
//
//    override fun clear() {
//        clearKnot.emit(ClearBookEvent.Clear)
//    }
//
//    private fun LoadBooksResult.toEvent() =
//        when (this) {
//            is LoadBooksResult.Success -> BooksEvent.Success(books)
//            is LoadBooksResult.Failure.Network -> BooksEvent.Failure("Network error. Check Internet connection and try again.")
//            LoadBooksResult.Failure.Generic -> BooksEvent.Failure("Generic error, please try again.")
//        }
//}