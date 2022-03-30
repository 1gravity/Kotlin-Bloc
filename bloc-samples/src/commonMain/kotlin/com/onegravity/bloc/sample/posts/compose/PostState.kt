package com.onegravity.bloc.sample.posts.compose

import com.github.michaelbull.result.Result
import com.onegravity.bloc.sample.posts.domain.repositories.Post

data class PostState(
    val loading: Boolean = false,
    val post: Result<Post, Throwable>? = null
)
