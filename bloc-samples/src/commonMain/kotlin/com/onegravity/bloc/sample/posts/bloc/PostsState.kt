package com.onegravity.bloc.sample.posts.bloc

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.Ok
import com.onegravity.bloc.sample.posts.domain.repositories.Post

data class PostsState(
    val loading: Boolean = false,
    val posts: Result<List<Post>, Throwable> = Ok(emptyList()),
) {
    fun isEmpty() = posts is Ok && posts.value.isEmpty()
}
