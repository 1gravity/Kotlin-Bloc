package com.onegravity.bloc.util

import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.sample.posts.compose.PostsComponent
import com.onegravity.bloc.sample.posts.compose.PostsComponentImpl
import com.onegravity.bloc.sample.posts.compose.PostsRootState
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.BlocState
import org.koin.core.component.KoinComponent

object PostsComponentIOS : KoinComponent {

    fun postsComponent(context: BlocContext): PostsComponent {
        val params = getKoinInstances<BlocState<PostsRootState, PostsRootState>, PostRepository>()
        return PostsComponentImpl(context, params.first, params.second)
    }

}
