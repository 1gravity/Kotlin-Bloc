package com.onegravity.bloc.sample.books

import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.Stream
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.ReduxBlocState
import com.onegravity.bloc.utils.Mapper
import com.onegravity.bloc.utils.toBlocState

/**
 * Implements the BooksUseCase with a single Bloc and a [ReduxBlocState].
 */
class BooksUseCaseImplRedux(context: BlocContext) : BooksUseCase {
    private val mapModel2State: Mapper<Books, BookState> = { books ->
        books.mapBoth(
            { if (it.isEmpty()) BookState.Empty else BookState.Loaded(it) },
            {
                val message = when (it) {
                    is ReduxFailure.Network -> "Network error. Check Internet connection and try again."
                    is ReduxFailure.Generic -> "Generic error, please try again."
                }
                BookState.Failure(message)
            }
        )
    }

    private val bloc = bloc<BookState, BookAction, ReduxAction>(
        context,
        reduxStore.toBlocState(
            context = context,
            initialState = BookState.Empty,
            selector = { it },
            mapper = mapModel2State
        )
    ) {
        reduce { _, action ->
            when (action) {
                BookAction.Load -> ReduxAction.Load
                else -> ReduxAction.Clear
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