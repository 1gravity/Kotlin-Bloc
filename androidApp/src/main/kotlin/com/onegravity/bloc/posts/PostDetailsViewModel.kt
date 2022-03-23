package com.onegravity.bloc.posts

import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.BlocOwner
import com.onegravity.bloc.sample.posts.bloc.Post
import com.onegravity.bloc.toLiveData
import com.onegravity.bloc.utils.Logger
import com.onegravity.bloc.utils.subscribe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.onegravity.bloc.sample.posts.bloc.PostDetailState
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview

class PostDetailsViewModel(context: ActivityBlocContext, postOverview: PostOverview) :
    BaseViewModel(context),
    BlocOwner<PostDetailState, Post.Action, Nothing, PostDetailState>,
    KoinComponent {

    override val bloc = Post.bloc(viewModelContext, postOverview)

    private val logger by inject<Logger>()

    init {
        bloc.subscribe(
            state = {
                logger.i("state: $it")
            },
            sideEffect = {
                logger.i("side effect: $it")
            }
        )
    }

    val state = toLiveData(bloc)

}
