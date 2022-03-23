package com.onegravity.bloc.sample.posts.bloc

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview

@Parcelize
data class PostListState(
    val loading: Boolean = false,
    val overviews: List<PostOverview> = emptyList(),
) : Parcelable
