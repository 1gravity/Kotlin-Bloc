package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.runCatching
import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.bloc
import com.onegravity.bloc.reduce
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.thunk
import com.onegravity.bloc.util.getKoinInstance
import com.onegravity.bloc.utils.Cancel
import com.onegravity.bloc.utils.JobConfig
import com.onegravity.bloc.utils.ThunkContextNoAction
import com.onegravity.bloc.utils.launch
import kotlinx.coroutines.yield
import kotlin.coroutines.cancellation.CancellationException

// no external actions, we use a simple function call
sealed class PostsAction

class PostsComponentImpl(context: BlocContext) : PostsComponent() {

    private val repository = getKoinInstance<PostRepository>()
    private val blocState = getKoinInstance<BlocState<PostsRootState, PostsRootState>>()

    // internal actions
    private data class PostsLoaded(val result: Result<List<Post>, Throwable>) : PostsAction()
    private data class PostLoaded(val result: Result<Post, Throwable>) : PostsAction()

    // we need to lazy initialize the Bloc so that the component is fully initialized before
    // making any calls to load the posts
    override val bloc by lazy {
        bloc<PostsRootState, PostsAction>(context, blocState) {
            onCreate {
                // example of "reducing" state from an initializer directly
                reduce(state.copy(postsState = state.postsState.copy(loading = true)))

                // we can access the db here because Dispatchers.Default is a Bloc's default
                // dispatcher, also we use Ktor which offloads the networking to another thread
                val result = repository.getOverviews()

                dispatch(PostsLoaded(result))
            }

            reduce<PostsLoaded> {
                state.copy(
                    postsState = state.postsState.copy(loading = false, posts = action.result)
                )
            }

            reduce<PostLoaded> {
                state.copy(postState = state.postState.copy(loadingId = null, post = action.result))
            }
        }
    }

    private var cancel: Cancel? = null

    override fun onSelected(post: Post) = thunk {
        // only load if it's not already being loaded or is not yet loaded
        val postState = getState().postState
        if (postState.loadingId == null || postState.loadingId != post.id ||
            postState.post?.component1()?.id != post.id)
        {
            // we cancel a previous loading job before starting a new one from the Bloc's
            // CoroutineScope -> it's also cancelled when the Bloc is stopped
            cancel = launch(JobConfig(true)) {
                load(post)
            }
        }
    }

    override fun onClosed() = reduce {
        cancel?.invoke()
        state.copy(postState = state.postState.copy(loadingId = null, post = null))
    }

    private suspend fun ThunkContextNoAction<PostsRootState, PostsAction, PostsRootState>.load(
        post: Post
    ) {
        runCatching {
            // example of "reducing" state from a thunk directly
            val newState = getState().run { copy(postState = postState.copy(loadingId = post.id)) }
            reduce(newState)
            val result = repository.getDetail(post.id)
            yield()
            dispatch(PostLoaded(result))
        }.onFailure {
            // this was launched with JobConfig(cancelPrevious = true) -> CancellationExceptions can
            // occur which we don't want to propagate to the ui
            if (it !is CancellationException) dispatch(PostLoaded(Err(it)))
        }
    }

}
