package com.onegravity.knot.sample.books

import com.onegravity.bloc.Stream
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import kotlinx.coroutines.delay

/**
 * Implements the BooksUseCase with a single [Bloc] and [Thunk]s
 */
class BooksUseCaseImplSimple(
    context: BlocContext,
    private val repository: BooksRepository,
) : BooksUseCase {

    private val bloc = bloc<BookState, BookEvent>(context, BookState.Empty) {
        thunkMatching<BookEvent.Load> { _, action, dispatch ->
            dispatch(BookEvent.Loading)
            delay(1000)
            val nextAction = repository.loadBooks().toAction()
            dispatch(nextAction)
        }

        // does the same as the thunk above
//        thunk { _, action, dispatch ->
//            if (action == BookEvent.Load) {
//                dispatch(BookEvent.Loading)
//                delay(1000)
//                val nextAction = repository.loadBooks().toAction()
//                dispatch(nextAction)
//            } else {
//                // without this no other action would reach the reducer
//                dispatch(action)
//            }
//        }

        reduce { state, action ->
            when (action) {
                BookEvent.Clear -> BookState.Empty
                BookEvent.Loading -> BookState.Loading
                is BookEvent.LoadComplete -> action.result.toState()
                else -> state
            }
        }
    }

    override val state: Stream<BookState>
        get() = bloc

    override fun load() {
        bloc.emit(BookEvent.Load)
    }

    override fun clear() {
        bloc.emit(BookEvent.Clear)
    }

}
