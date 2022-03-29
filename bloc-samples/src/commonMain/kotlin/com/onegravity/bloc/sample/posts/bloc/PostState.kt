package com.onegravity.bloc.sample.posts.bloc

import com.github.michaelbull.result.Result
import com.onegravity.bloc.sample.posts.domain.repositories.PostDetail
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview

data class PostState(
    val loading: Boolean = false,
    val postOverview: PostOverview? = null,
    val post: Result<PostDetail, Exception>? = null
)

