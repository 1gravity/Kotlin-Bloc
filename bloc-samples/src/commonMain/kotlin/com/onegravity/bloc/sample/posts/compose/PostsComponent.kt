package com.onegravity.bloc.sample.posts.compose

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.onegravity.bloc.utils.BlocOwner

interface PostsComponent : BlocOwner<PostsRootState, Any, Unit, PostsRootState>, InstanceKeeper.Instance {
    fun onClicked(post: Post)
    fun onClosed()
    fun loadPosts()
    fun loadPost()
}
