package com.onegravity.bloc.posts

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.*
import com.onegravity.bloc.sample.posts.bloc.Posts
import com.onegravity.bloc.sample.posts.bloc.PostsState
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.utils.BlocOwner
import org.koin.core.component.KoinComponent

class PostsViewModel(context: ActivityBlocContext) :
    ViewModel(),
    BlocOwner<PostsState, Posts.Action, Posts.OpenPost, PostsState>,
    KoinComponent {

    override val bloc = Posts.bloc(blocContext(context))

    fun onPostClicked(post: Post) = sideEffect {
        Posts.OpenPost(post)
    }

    // this does the same
//    fun onPostClicked(post: Post) {
//        bloc.send(Posts.Action.Clicked(post))
//    }

    // so does this
//    fun onPostClicked(post: Post) {
//        clicked(post)
//        // or
//        bloc.clicked(post)
//    }

    // and this one too (no BlocOwner interface needed)
//    fun onPostClicked(post: Post) = bloc.sideEffect {
//        Posts.OpenPost(post)
//    }

}

/**
 * This PostListViewModel does the same as the one above but instead of sending actions to the Bloc,
 * it implements the thunk { }, reduce { } and sideEffect { } functions directly. This is similar to
 * what Orbit MVI does (https://github.com/orbit-mvi/orbit-mvi).
 */
//class PostListViewModel : ViewModel(),
//    BlocOwner<PostsState, Nothing, Posts.OpenPost, PostsState>,
//    KoinComponent {
//
//    private val repository = getKoinInstance<PostRepository>()
//
//    override val bloc = bloc<PostsState, Nothing, Posts.OpenPost, PostsState>(
//        blocContext(),
//        blocState(PostsState())
//    )
//
//    init {
//        onCreate { if (state.isEmpty()) load() }
//    }
//
//    private fun load() = thunk {
//        loading()
//        loaded(repository.getOverviews())
//    }
//
//    private fun loading() = reduce {
//        state.copy(loading = true)
//    }
//
//    private fun loaded(posts: Result<List<Post>, Exception>) = reduce {
//        state.copy(loading = false, posts = posts)
//    }
//
//    fun onPostClicked(post: Post) = sideEffect {
//        Posts.OpenPost(post)
//    }
//
//}