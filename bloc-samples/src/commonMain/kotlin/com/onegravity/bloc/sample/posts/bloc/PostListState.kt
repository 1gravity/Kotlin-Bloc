package com.onegravity.bloc.sample.posts.bloc

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.Ok
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview

data class PostListState(
    val loading: Boolean = false,
    val overviews: Result<List<PostOverview>, Exception> = Ok(emptyList()),
) {
    fun isEmpty() = overviews is Ok && overviews.value.isEmpty()
}
