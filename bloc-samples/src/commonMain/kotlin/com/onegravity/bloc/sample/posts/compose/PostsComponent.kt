package com.onegravity.bloc.sample.posts.compose

import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.utils.BlocOwner

interface PostsComponent : BlocOwner<PostsRootState, PostsAction, Unit, PostsRootState> {
    fun onClicked(post: Post)
    fun onClosed()
    fun loadPosts()
    fun loadPost()
}
