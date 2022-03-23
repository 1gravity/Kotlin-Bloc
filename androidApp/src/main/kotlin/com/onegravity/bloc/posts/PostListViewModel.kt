package com.onegravity.bloc.posts

import com.onegravity.bloc.*
import com.onegravity.bloc.sample.posts.bloc.PostList
import com.onegravity.bloc.sample.posts.bloc.PostListState
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview
import com.onegravity.bloc.utils.BlocObservableOwner
import com.onegravity.bloc.utils.toObservable
import org.koin.core.component.KoinComponent

class PostListViewModel(context: ActivityBlocContext) :
    BaseViewModel(context),
    BlocObservableOwner<PostListState, PostList.OpenPost>,
    KoinComponent {

    private val bloc = PostList.bloc(viewModelContext)

    override val observable = bloc.toObservable()

    fun onPostClicked(post: PostOverview) {
        bloc.emit(PostList.Action.Clicked(post))
    }

}