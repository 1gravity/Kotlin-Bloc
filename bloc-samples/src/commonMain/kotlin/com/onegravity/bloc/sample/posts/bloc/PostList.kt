package com.onegravity.bloc.sample.posts.bloc

import com.github.michaelbull.result.Result
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.util.getKoinInstance
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository

object PostList {
    sealed class Action {
        object Load : Action()
        object Loading : Action()
        data class Loaded(val postList: Result<List<PostOverview>, Exception>) : Action()
        data class Clicked(val post: PostOverview) : Action()
    }

    class OpenPost(val postOverview: PostOverview)

    fun bloc(context: BlocContext) = bloc<PostListState, Action, OpenPost, PostListState>(
        context,
        blocState(PostListState())
    ) {
        val repository = getKoinInstance<PostRepository>()

        onCreate { if (state.isEmpty()) dispatch(Action.Load) }

        // we could also put the thunk code into the onCreate block but we want to illustrate the
        // ability to use initializing code
        thunk<Action.Load> {
            dispatch(Action.Loading)
            val result = repository.getOverviews()
            dispatch(Action.Loaded(result))
        }

        sideEffect<Action.Clicked> { OpenPost(action.post) }

        reduce<Action.Loading> { state.copy(loading = true) }

        reduce<Action.Loaded> {
            state.copy(loading = false, overviews = action.postList)
        }
    }
}
