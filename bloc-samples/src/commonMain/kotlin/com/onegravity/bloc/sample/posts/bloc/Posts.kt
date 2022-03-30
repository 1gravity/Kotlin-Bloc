package com.onegravity.bloc.sample.posts.bloc

import com.github.michaelbull.result.Result
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.util.getKoinInstance
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.utils.BlocOwner

object Posts {
    // you can either send actions to the Bloc directly or call these functions instead
    fun PostsBloc.load() = send(Action.Load)
    fun PostsBloc.clicked(post: Post) = send(Action.Clicked(post))

    fun BlocOwner<PostsState, Action, OpenPost, PostsState>.clicked(post: Post) = bloc.clicked(post)

    sealed class Action {
        object Load : Action()
        object Loading : Action()
        data class Loaded(val posts: Result<List<Post>, Exception>) : Action()
        data class Clicked(val post: Post) : Action()
    }

    class OpenPost(val post: Post)

    fun bloc(context: BlocContext) = bloc<PostsState, Action, OpenPost, PostsState>(
        context,
        blocState(PostsState())
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
            state.copy(loading = false, posts = action.posts)
        }
    }
}
