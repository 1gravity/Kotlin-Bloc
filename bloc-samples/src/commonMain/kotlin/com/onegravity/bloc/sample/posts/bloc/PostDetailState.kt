package com.onegravity.bloc.sample.posts.bloc

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.onegravity.bloc.sample.posts.domain.repositories.PostDetail
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview

@Parcelize
data class PostDetailState(
    val postOverview: PostOverview,
    val loading: Boolean = false,
    val post: PostDetail? = null
) : Parcelable

