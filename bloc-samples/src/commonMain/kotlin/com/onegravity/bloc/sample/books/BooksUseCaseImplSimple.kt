package com.onegravity.bloc.sample.books

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

    private val bloc = bloc<BookState, BookAction>(context, BookState.Empty) {
        thunkMatching<BookAction.Load> { _, _, dispatch ->
            dispatch(BookAction.Loading)
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
                BookAction.Clear -> BookState.Empty
                BookAction.Loading -> BookState.Loading
                is BookAction.LoadComplete -> action.result.toState()
                else -> state
            }
        }
    }

    override val state: Stream<BookState>
        get() = bloc

    override fun load() {
        bloc.emit(BookAction.Load)
    }

    override fun clear() {
        bloc.emit(BookAction.Clear)
    }

}
