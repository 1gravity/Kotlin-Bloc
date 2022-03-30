package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.Ok

data class PostsRootState(
    val selectedPost: Int? = null,
    val postsState: PostsState,
    val postState: PostState,
) {
    fun isEmpty() = postsState.posts is Ok && postsState.posts.value.isEmpty()
}
