//package com.onegravity.knot.sample.booksRedux
//
//import org.reduxkotlin.createThreadSafeStore
//import com.github.michaelbull.result.*
//import com.onegravity.knot.sample.books.Book
//import com.onegravity.knot.sample.books.IBooksRepository
//import org.reduxkotlin.Thunk
//import org.reduxkotlin.applyMiddleware
//import org.reduxkotlin.createThunkMiddleware
//
//typealias Books = Result<List<Book>, String>
//
//class BookStore(private val repository: IBooksRepository) {
//
//    val reduxStore = createThreadSafeStore(
//        reducer = ::rootReducer,
//        preloadedState = Ok(emptyList()),
//        enhancer = applyMiddleware(createThunkMiddleware())
//    )
//
//    fun fooThunk(query: String): Thunk<Books> = { dispatch, getState, extraArg ->
//        dispatch(Actions.Loading)
//        launch {
//            val result = api.foo(query)
//            if (result.isSuccessful()) {
//                dispatch(FetchFooSuccess(result.payload)
//            } else {
//                dispatch(FetchFooFailure(result.message)
//            }
//        }
//    }
//
//    sealed class Actions {
//        object Loading : Actions()
//        object LoadBooks : Actions()
//        object ClearBooks : Actions()
//    }
//
//    private fun rootReducer(state: Books, action: Any) =
//        when (action) {
//            Actions.LoadBooks -> loadBooks(state)
//            Actions.ClearBooks -> Ok(emptyList())
//            else -> throw IllegalArgumentException("Invalid action $action")
//        }
//
//    private fun loadBooks(books: Books): Result<List<Book>, String> =
//        books
//            .onFailure { loadBooks() }
//            .onSuccess { it.ifEmpty { loadBooks() } }
//
//    private fun loadBooks() =
//        when (val loadResult = repository.loadBooks()) {
//            is IBooksRepository.LoadBooksResult.Success -> Ok(loadResult.books)
//            is IBooksRepository.LoadBooksResult.Failure.Network -> Err("Network error. Check Internet connection and try again.")
//            IBooksRepository.LoadBooksResult.Failure.Generic -> Err("Generic error, please try again.")
//        }
//
//}
