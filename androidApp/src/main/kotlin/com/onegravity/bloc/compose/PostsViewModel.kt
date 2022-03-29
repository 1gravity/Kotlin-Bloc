package com.onegravity.bloc.compose

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

class PostsViewModel :
    ViewModel(),
    BlocOwner<PostListState, PostList.Action, PostList.OpenPost, PostListState>,
    KoinComponent {

    override val bloc = PostList.bloc(blocContext())

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
