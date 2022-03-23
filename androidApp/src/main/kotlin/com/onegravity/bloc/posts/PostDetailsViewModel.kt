package com.onegravity.bloc.posts

import com.onegravity.bloc.*
import com.onegravity.bloc.sample.posts.bloc.Post
import com.onegravity.bloc.sample.posts.bloc.PostDetailState
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview
import com.onegravity.bloc.utils.BlocObservableOwner
import com.onegravity.bloc.utils.toObservable
import org.koin.core.component.KoinComponent

class PostDetailsViewModel(context: ActivityBlocContext, postOverview: PostOverview) :
    BaseViewModel(context),
    BlocObservableOwner<PostDetailState, Nothing>,
    KoinComponent {

    override val observable = Post.bloc(viewModelContext, postOverview).toObservable()

}
