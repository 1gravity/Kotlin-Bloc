package com.onegravity.bloc.sample.posts.bloc

import com.github.michaelbull.result.Result
import com.onegravity.bloc.sample.posts.domain.repositories.PostDetail
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview

data class PostState(
    val postOverview: PostOverview,
    val loading: Boolean = false,
    val post: Result<PostDetail, Exception>? = null
)

