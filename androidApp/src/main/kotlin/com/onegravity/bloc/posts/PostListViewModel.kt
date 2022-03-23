package com.onegravity.bloc.posts

import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.BlocOwner
import com.onegravity.bloc.sample.posts.bloc.PostList
import com.onegravity.bloc.sample.posts.bloc.PostListState
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview
import com.onegravity.bloc.toLiveData
import org.koin.core.component.KoinComponent

class PostListViewModel(context: ActivityBlocContext) :
    BaseViewModel(context),
    BlocOwner<PostListState, PostList.Action, PostList.OpenPost, PostListState>,
    KoinComponent {

    override val bloc = PostList.bloc(viewModelContext)

    val state = toLiveData(bloc)

    fun onPostClicked(post: PostOverview) {
        bloc.emit(PostList.Action.Clicked(post))
    }

}