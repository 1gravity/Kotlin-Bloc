package com.onegravity.bloc.posts

import com.onegravity.bloc.ActivityBlocContext
import com.onegravity.bloc.BaseViewModel
import com.onegravity.bloc.BlocOwner
import com.onegravity.bloc.sample.posts.PostList
import com.onegravity.bloc.toLiveData
import com.onegravity.bloc.utils.Logger
import com.onegravity.bloc.utils.subscribe
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.orbitmvi.orbit.sample.posts.app.features.postlist.viewmodel.PostListState
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostOverview

class PostListViewModel(context: ActivityBlocContext) :
    BaseViewModel(context),
    BlocOwner<PostListState, PostList.Action, PostList.OpenPost, PostListState>,
    KoinComponent {

    override val bloc = PostList.bloc(viewModelContext)

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

    fun onPostClicked(post: PostOverview) {
        bloc.emit(PostList.Action.Clicked(post))
    }

}