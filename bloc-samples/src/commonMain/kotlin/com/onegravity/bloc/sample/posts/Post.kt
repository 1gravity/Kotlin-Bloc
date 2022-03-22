package com.onegravity.bloc.sample.posts

import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.util.getKoinInstance
import com.onegravity.bloc.utils.Logger
import org.orbitmvi.orbit.sample.posts.app.features.postdetails.viewmodel.PostDetailState
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostDetail
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostOverview
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostRepository

object Post {
    sealed class Action {
        class Load(id: Int) : Action()
        object Loading : Action()
        data class Loaded(val post: PostDetail?) : Action()
    }

    fun bloc(context: BlocContext, overview: PostOverview) = bloc<PostDetailState, Action>(
        context,
        PostDetailState(overview)
    ) {
        val repository = getKoinInstance<PostRepository>()
        val logger = getKoinInstance<Logger>()

        init {
            dispatch(Action.Load(overview.id))
        }

        thunk<Action.Load> {
            logger.d("thunk 1 started: $action")
            dispatch(Action.Loading)
            val postOverview = getState().postOverview
            val result = repository.getDetail(postOverview.id)
                .mapBoth({ it }, { null })
            dispatch(Action.Loaded(result))
        }

        state<Action.Loading> { state.copy(loading = true) }

        state<Action.Loaded> {
            state.copy(loading = false, post = (action as Action.Loaded).post)
        }
    }
}
