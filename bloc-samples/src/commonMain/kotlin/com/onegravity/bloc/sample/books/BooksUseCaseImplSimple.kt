package com.onegravity.bloc.sample.books

import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.bloc
import com.onegravity.bloc.toObservable

/**
 * Implements the BooksUseCase with a single Bloc and Thunks
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

        // this does the same as the thunk above
        // note that we explicitly need to handle the else case and dispatch = forward the action
        // we didn't handle in the thunk
//        thunk {
//            if (action == BookAction.Load) {
//                dispatch(BookAction.Loading)
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
