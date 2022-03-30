package com.onegravity.bloc.sample.posts.bloc

import com.github.michaelbull.result.Result
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.util.getKoinInstance

object Post {
    // you can either send actions to the Bloc directly or call these functions instead
    fun PostBloc.load(postId: Int) = send(Action.Load(postId))

    sealed class Action {
        data class Load(val postId: Int) : Action()
        object Loading : Action()
        data class Loaded(val post: Result<Post, Throwable>) : Action()
    }

    fun bloc(context: BlocContext): PostBloc = bloc(context, PostState()) {
        val repository = getKoinInstance<PostRepository>()

        thunk<Action.Load> {
            dispatch(Action.Loading)
            val result = repository.getDetail(action.postId)
            dispatch(Action.Loaded(result))
        }

        reduce<Action.Loading> { state.copy(loading = true) }

        reduce<Action.Loaded> {
            state.copy(loading = false, post = action.post)
        }
    }
}
