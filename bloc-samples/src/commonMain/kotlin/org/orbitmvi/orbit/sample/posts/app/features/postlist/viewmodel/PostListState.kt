package org.orbitmvi.orbit.sample.posts.app.features.postlist.viewmodel

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostOverview

@Parcelize
data class PostListState(
    val loading: Boolean = false,
    val overviews: List<PostOverview> = emptyList(),
) : Parcelable
