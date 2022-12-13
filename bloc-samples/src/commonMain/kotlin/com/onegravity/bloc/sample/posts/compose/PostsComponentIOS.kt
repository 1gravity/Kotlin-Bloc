package com.onegravity.bloc.sample.posts.compose

import com.onegravity.bloc.BlocContext
import com.onegravity.bloc.sample.posts.domain.repositories.PostRepository
import com.onegravity.bloc.state.BlocState
import com.onegravity.bloc.util.getKoinInstances
import org.koin.core.component.KoinComponent

/**
 * We need a wrapper for iOS to use the component
 */
object PostsComponentIOS : KoinComponent {

    fun postsComponent(context: BlocContext): PostsComponent {
        val params = getKoinInstances<BlocState<PostsRootState, PostsRootState>, PostRepository>()
        return PostsComponentImpl(context, params.first, params.second)
    }

}
