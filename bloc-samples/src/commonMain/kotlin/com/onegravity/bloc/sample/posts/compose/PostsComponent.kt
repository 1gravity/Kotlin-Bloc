package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.Ok
import com.onegravity.bloc.*
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.util.getKoinInstance
import com.onegravity.bloc.utils.BlocOwner

data class State(
    val selectedPost: Int? = null,
    val postsState: PostsState,
    val postState: PostState,
) {
    fun isEmpty() = postsState.posts is Ok && postsState.posts.value.isEmpty()
}

data class PostsState(
    val loading: Boolean = false,
    val posts: Result<List<Post>, Exception> = Ok(emptyList()),
)

data class PostState(
    val loading: Boolean = false,
    val post: Result<Post, Exception>? = null
)

interface PostsComponent : BlocOwner<State, Any, Unit, State> {
    fun onClicked(post: Post)
    fun onClosed()
    fun loadPosts()
    fun loadPost()
}

class PostsComponentImpl(context: BlocContext) : PostsComponent {
    sealed class Action {
        object LoadingPosts : Action()
        data class LoadedPosts(val result: Result<List<Post>, Exception>) : Action()

        object LoadingPost : Action()
        data class LoadedPost(val result: Result<Post, Exception>) : Action()
    }

    private val repository = getKoinInstance<PostRepository>()

    override val bloc = bloc<State, Any>(
        context,
        blocState(State(postsState = PostsState(), postState = PostState()))
    ) {
        reduce<Action.LoadingPosts> {
            state.copy(postsState = state.postsState.copy(loading = true))
        }

        reduce<Action.LoadedPosts> {
            state.copy(postsState = state.postsState.copy(loading = false, posts = action.result))
        }

        reduce<Action.LoadingPost> {
            state.copy(postState = state.postState.copy(loading = true))
        }

        reduce<Action.LoadedPost> {
            state.copy(postState = state.postState.copy(loading = false, post = action.result))
        }
    }

    init {
        onCreate {
            if (state.isEmpty()) loadPosts()
        }
    }

    override fun onClicked(post: Post) {
        reduce { state.copy(selectedPost = post.id) }
        loadPost()
    }

    override fun onClosed() = reduce {
        state.copy(selectedPost = null)
    }

    override fun loadPosts() = thunk {
        dispatch(Action.LoadingPosts)
        val result = repository.getOverviews()
        dispatch(Action.LoadedPosts(result))
    }

    override fun loadPost() = thunk {
        val id = getState().selectedPost
        if (id != null) {
            dispatch(Action.LoadingPost)
            val result = repository.getDetail(id)
            dispatch(Action.LoadedPost(result))
        }
    }

}
