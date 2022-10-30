package com.onegravity.bloc.sample.posts.compose

import com.onegravity.bloc.Bloc
import com.onegravity.bloc.BlocOwner
import com.onegravity.bloc.sample.posts.domain.repositories.Post

abstract class PostsComponent : BlocOwner<PostsRootState, PostsAction, Unit, PostsRootState> {
    // DON'T REMOVE: we need to repeat this for iOS to prevent type erasure for generics
    abstract override val bloc: Bloc<PostsRootState, PostsAction, Unit>

    abstract fun onSelected(post: Post)

    abstract fun onClosed()
}
