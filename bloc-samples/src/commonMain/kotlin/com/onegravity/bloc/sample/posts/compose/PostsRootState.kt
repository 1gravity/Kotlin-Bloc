package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.Ok

data class PostsRootState(
    val selectedPost: Int? = null,
    val postsState: PostsState,
    val postState: PostState,
) {
    fun postsAreLoaded() = postsState.posts is Ok && postsState.posts.value.isNotEmpty()
    fun postIsLoaded() = selectedPost != null && postState.post is Ok && postState.post.value.id == selectedPost
}
