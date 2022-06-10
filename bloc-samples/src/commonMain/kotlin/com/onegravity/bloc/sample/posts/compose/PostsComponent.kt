package com.onegravity.bloc.sample.posts.compose

import com.onegravity.bloc.Bloc
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.BlocOwner

abstract class PostsComponent : BlocOwner<PostsRootState, PostsAction, Unit, PostsRootState> {
    // we need to repeat this for iOS to prevent erasure of generic types ...
    abstract override val bloc: Bloc<PostsRootState, PostsAction, Unit>
    abstract fun onSelected(post: Post)
    abstract fun onClosed()
}
