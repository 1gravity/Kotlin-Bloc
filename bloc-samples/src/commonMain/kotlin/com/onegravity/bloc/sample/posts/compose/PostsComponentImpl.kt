package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import com.onegravity.bloc.*
import com.onegravity.bloc.internal.*
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.reduce
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.thunk
import com.onegravity.bloc.util.getKoinInstance
import com.onegravity.bloc.utils.ThunkContextNoAction
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

// no external actions, we use a simple function call
sealed class PostsAction

class PostsComponentImpl(context: BlocContext) : PostsComponent() {

    private val repository = getKoinInstance<PostRepository>()
    private val blocState = getKoinInstance<BlocState<PostsRootState, PostsRootState>>()

    // internal actions
    private object PostsLoading : PostsAction()
    private data class PostsLoaded(val result: Result<List<Post>, Throwable>) : PostsAction()
    private class PostLoading(val postId: Int) : PostsAction()
    private data class PostLoaded(val result: Result<Post, Throwable>) : PostsAction()

    // we need to lazy initialize the Bloc so that the component is fully initialized before
    // making any calls to load the posts
    override val bloc by lazy {
        bloc<PostsRootState, PostsAction>(context, blocState) {
            onCreate {
                dispatch(PostsLoading)
                // we can access the db here because Dispatchers.Default is a Bloc's default dispatcher
                // also we use Ktor which offloads the networking to another thread
                val result = repository.getOverviews()
                dispatch(PostsLoaded(result))
            }

            reduce<PostsLoading> {
                state.copy(postsState = state.postsState.copy(loading = true))
            }

            reduce<PostsLoaded> {
                state.copy(postsState = state.postsState.copy(loading = false, posts = action.result))
            }

            reduce<PostLoading> {
                state.copy(postState = state.postState.copy(loadingId = action.postId))
            }

            reduce<PostLoaded> {
                state.copy(postState = state.postState.copy(loadingId = null, post = action.result))
            }
        }
    }

    override fun onSelected(post: Post) = thunk {
        // only load if it's not already being loaded or is not yet loaded
        val postState = getState().postState
        if (postState.loadingId == null || postState.loadingId != post.id || postState.post?.component1()?.id != post.id) {
            // we cancel a previous loading job before starting a new one from the Bloc's
            // CoroutineScope (so it's cancelled when the Bloc is stopped)
            launchIt(cancelBeforeLaunch = true) {
                load(post)
            }
        }
    }

    override fun onClosed() = reduce {
        state.copy(postState = state.postState.copy(loadingId = null, post = null))
    }

    private suspend fun ThunkContextNoAction<PostsRootState, PostsAction>.load(post: Post) {
        runCatching {
            dispatch(PostLoading(post.id))
            val result = repository.getDetail(post.id)
            dispatch(PostLoaded(result))
        }.onFailure {
            dispatch(PostLoaded(Err(it)))
        }
    }
}
