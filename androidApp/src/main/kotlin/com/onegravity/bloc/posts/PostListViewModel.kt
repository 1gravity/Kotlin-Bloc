package com.onegravity.bloc.posts

import androidx.lifecycle.ViewModel
import com.onegravity.bloc.*
import com.onegravity.bloc.sample.posts.bloc.PostList
import com.onegravity.bloc.sample.posts.bloc.PostListState
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview
import com.onegravity.bloc.utils.BlocOwner
import org.koin.core.component.KoinComponent
import com.onegravity.bloc.bloc
import com.github.michaelbull.result.Result
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.blocState
import com.onegravity.bloc.util.getKoinInstance

class PostListViewModel(context: ActivityBlocContext) :
    ViewModel(),
    BlocOwner<PostListState, PostList.Action, PostList.OpenPost, PostListState>,
    KoinComponent {

    override val bloc = PostList.bloc(blocContext(context))

    fun onPostClicked(post: PostOverview) = sideEffect {
        PostList.OpenPost(post)
    }

    // this does the same
//    fun onPostClicked(post: PostOverview) {
//        bloc.send(PostList.Action.Clicked(post))
//    }

    // as does this (no BlocOwner interface needed)
//    fun onPostClicked(post: PostOverview) = bloc.sideEffect {
//        PostList.OpenPost(post)
//    }

}

/**
 * This PostListViewModel does the same as the one above but instead of sending actions to the Bloc,
 * it implements the thunk { }, reduce { } and sideEffect { } functions directly. This is similar to
 * what Orbit MVI does (https://github.com/orbit-mvi/orbit-mvi).
 */
//class PostListViewModel2(context: ActivityBlocContext) :
//    ViewModel(),
//    BlocOwner<PostListState, Nothing, PostList.OpenPost, PostListState>,
//    KoinComponent {
//
//    private val repository = getKoinInstance<PostRepository>()
//
//    override val bloc = bloc<PostListState, Nothing, PostList.OpenPost, PostListState>(
//        blocContext(context),
//        blocState(PostListState())
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
//    private fun loaded(postList: Result<List<PostOverview>, Exception>) = reduce {
//        state.copy(loading = false, overviews = postList)
//    }
//
//    fun onPostClicked(post: PostOverview) = sideEffect {
//        PostList.OpenPost(post)
//    }
//
//}