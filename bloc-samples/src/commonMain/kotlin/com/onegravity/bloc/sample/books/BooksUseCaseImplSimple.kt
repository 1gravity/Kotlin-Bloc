package com.onegravity.bloc.sample.books

import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.toObservable

/**
 * Implements the BooksUseCase with a single [Bloc] and [Thunk]s
 */
class BooksUseCaseImplSimple(
    context: BlocContext,
    private val repository: BooksRepository,
) : BooksUseCase {

    private val bloc = bloc<BookState, BookAction>(context, BookState.Empty) {
        thunk<BookAction.Load> {
            dispatch(BookAction.Loading)
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

        reduce<BookAction.Clear> { BookState.Empty }

        reduce<BookAction.Loading> { BookState.Loading }

        reduce<BookAction.LoadComplete> { action.result.toState() }
    }

    override fun load() {
        bloc.send(BookAction.Load)
    }

    override fun clear() {
        bloc.send(BookAction.Clear)
    }

    override val observable = bloc.toObservable()

}
