package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.*
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.bloc
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.reduce
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.thunk
import com.onegravity.bloc.util.getKoinInstance
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostsComponentImpl(context: BlocContext) : PostsComponent {
    private val blocState =
        blocState(PostsRootState(postsState = PostsState(), postState = PostState()))

    sealed class Action {
        object LoadingPosts : Action()
        data class LoadedPosts(val result: Result<List<Post>, Throwable>) : Action()

        class LoadingPost(val postId: Int) : Action()
        data class LoadedPost(val result: Result<Post, Throwable>) : Action()
    }

    private val repository = getKoinInstance<PostRepository>()

    override val bloc: Bloc<PostsRootState, Any, Unit> by lazy {
        bloc<PostsRootState, Any>(context, blocState) {
            onCreate {
                if (!state.postsAreLoaded()) loadPosts()
            }

            reduce<Action.LoadingPosts> {
                state.copy(postsState = state.postsState.copy(loading = true))
            }

            reduce<Action.LoadedPosts> {
                state.copy(postsState = state.postsState.copy(loading = false, posts = action.result))
            }

            reduce<Action.LoadingPost> {
                state.copy(postState = state.postState.copy(loading = action.postId))
            }

            reduce<Action.LoadedPost> {
                when (action.result is Ok) {
                    true -> state.copy(postState = state.postState.copy(loading = null, post = action.result))
                    else -> state.copy(postState = state.postState.copy(loading = null, post = action.result))
                }
            }
        }
    }

    private var loadingJob: Job? = null
    override fun onClicked(post: Post) = reduce {
        when {
            state.selectedPost == null || state.selectedPost != post.id -> state.copy(selectedPost = post.id)
                // the state was already reduced (synchronously so selectedPost == post.id)
                .also {
                    // we cancel a previous loading job before starting a new one from the Bloc's
                    // CoroutineScope (so it's cancelled when the Bloc is destroyed)
                    loadingJob?.cancel()
                    loadingJob = coroutineScope.launch {
                        loadPost(this)
                    }
                }
            else -> state
        }
    }

    override fun onClosed() = reduce {
        state.copy(selectedPost = null)
    }

    override fun loadPosts() = thunk {
        dispatch(Action.LoadingPosts)
        val result = repository.getOverviews()
        dispatch(Action.LoadedPosts(result))
    }

    override fun loadPost() = loadPost(null)

    private fun loadPost(coroutineScope: CoroutineScope?) = thunk(coroutineScope) {
        runCatching {
            val id = getState().selectedPost
            if (id != null) {
                dispatch(Action.LoadingPost(id))
                val result = repository.getDetail(id)
                dispatch(Action.LoadedPost(result))
            }
        }.onFailure {
            when (it is CancellationException) {
                true -> reduce { state.copy(postsState = state.postsState.copy(loading = false)) }
                else -> dispatch(Action.LoadedPost(Err(it)))
            }
        }
    }

}
