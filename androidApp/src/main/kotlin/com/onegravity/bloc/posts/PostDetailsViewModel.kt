package com.onegravity.bloc.posts

import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.BlocOwner
import com.onegravity.bloc.sample.posts.Post
import com.onegravity.bloc.toLiveData
import com.onegravity.bloc.utils.Logger
import com.onegravity.bloc.utils.subscribe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.sample.posts.app.features.postdetails.viewmodel.PostDetailState
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostOverview

class PostDetailsViewModel(context: ActivityBlocContext, postOverview: PostOverview) :
    BaseViewModel(context),
    BlocOwner<PostDetailState, Post.Action, Nothing, PostDetailState>,
    KoinComponent {

    override val bloc = Post.bloc(viewModelContext, postOverview)

    private val logger by inject<Logger>()

    init {
        logger.i("HELLO WORLD")
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
