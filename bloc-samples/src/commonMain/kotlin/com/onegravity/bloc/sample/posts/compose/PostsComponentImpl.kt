package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.*
import com.onegravity.bloc.*
import com.onegravity.bloc.context.BlocContext
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.util.getKoinInstance
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostsComponentImpl(context: BlocContext) : PostsComponent {
    private val blocState = getKoinInstance<BlocState<PostsRootState, PostsRootState>>()

    private val repository = getKoinInstance<PostRepository>()

    override val bloc: Bloc<PostsRootState, PostsAction, Unit> by lazy {
        bloc<PostsRootState, PostsAction>(context, blocState) {
            onCreate {
                if (!state.postsAreLoaded()) loadPosts()
            }

            reduce<PostsAction.LoadingPosts> {
                state.copy(postsState = state.postsState.copy(loading = true))
            }

            reduce<PostsAction.LoadedPosts> {
                state.copy(postsState = state.postsState.copy(loading = false, posts = action.result))
            }

            reduce<PostsAction.LoadingPost> {
                state.copy(postState = state.postState.copy(loading = action.postId))
            }

            reduce<PostsAction.LoadedPost> {
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
                    // CoroutineScope (so it's cancelled when the Bloc is stopped)
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
        dispatch(PostsAction.LoadingPosts)
        val result = repository.getOverviews()
        dispatch(PostsAction.LoadedPosts(result))
    }

    override fun loadPost() = loadPost(null)

    private fun loadPost(coroutineScope: CoroutineScope?) = thunk(coroutineScope) {
        runCatching {
            val id = getState().selectedPost
            if (id != null) {
                dispatch(PostsAction.LoadingPost(id))
                val result = repository.getDetail(id)
                dispatch(PostsAction.LoadedPost(result))
            }
        }.onFailure {
            when (it is CancellationException) {
                true -> reduce { state.copy(postsState = state.postsState.copy(loading = false)) }
                else -> dispatch(PostsAction.LoadedPost(Err(it)))
            }
        }
    }

}
