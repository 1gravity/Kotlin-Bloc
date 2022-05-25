package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.Ok

data class PostsRootState(
    val postsState: PostsState,
    val postState: PostState,
) {
    fun postsAreLoaded() = postsState.posts is Ok && postsState.posts.value.isNotEmpty()
    fun postIsLoading() = postState.loadingId != null
    fun postIsLoaded() = postState.post is Ok
    fun selectedPost() = postState.loadingId ?: postState.post?.component1()?.id
}
