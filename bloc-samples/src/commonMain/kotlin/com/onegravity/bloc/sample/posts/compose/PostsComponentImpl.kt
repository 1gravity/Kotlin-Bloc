package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.Result
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.onCreate
import com.onegravity.bloc.reduce
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.thunk
import com.onegravity.bloc.util.getKoinInstance

class PostsComponentImpl(context: BlocContext) : PostsComponent {
    private val blocState = blocState(PostsRootState(postsState = PostsState(), postState = PostState()))

    sealed class Action {
        object LoadingPosts : Action()
        data class LoadedPosts(val result: Result<List<Post>, Throwable>) : Action()

        object LoadingPost : Action()
        data class LoadedPost(val result: Result<Post, Throwable>) : Action()
    }

    private val repository = getKoinInstance<PostRepository>()

    override val bloc = bloc<PostsRootState, Any>(context, blocState) {
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

    override fun onDestroy() {
        // todo
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
