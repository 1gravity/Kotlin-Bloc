package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.Result
import com.onegravity.bloc.sample.posts.domain.repositories.Post

sealed class PostsAction {
    object LoadingPosts : PostsAction()
    data class LoadedPosts(val result: Result<List<Post>, Throwable>) : PostsAction()

    class LoadingPost(val postId: Int) : PostsAction()
    data class LoadedPost(val result: Result<Post, Throwable>) : PostsAction()
}
