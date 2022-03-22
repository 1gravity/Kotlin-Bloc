package com.onegravity.bloc.sample.posts

import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.util.getKoinInstance
import com.onegravity.bloc.utils.Logger
import org.orbitmvi.orbit.sample.posts.app.features.postlist.viewmodel.PostListState
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostDetail
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostOverview
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostRepository

object PostList {
    sealed class Action {
        object Load : Action()
        object Loading : Action()
        data class Loaded(val postList: List<PostOverview>) : Action()
        data class Clicked(val post: PostOverview) : Action()
    }

    class OpenPost(val postOverview: PostOverview)

    fun bloc(context: BlocContext) = bloc<PostListState, Action, OpenPost, PostListState>(
        context,
        blocState(PostListState())
    ) {
        val repository = getKoinInstance<PostRepository>()
        val logger = getKoinInstance<Logger>()

        init {
            dispatch(Action.Load)
        }

        thunk<Action.Load> {
            logger.d("thunk<Action.Load> started: $action")
            dispatch(Action.Loading)
            val result = repository.getOverviews().mapBoth({ it }, { emptyList() })
            dispatch(Action.Loaded(result))
        }

        // TODO investigate if we can eliminate the type cast
        sideEffect<Action.Clicked> {
            OpenPost((action as Action.Clicked).post)
        }

        // TODO consider the concept of a proposal...

        state<Action.Loading> { state.copy(loading = true) }

        state<Action.Loaded> {
            state.copy(loading = false, overviews = (action as Action.Loaded).postList)
        }
    }
}
