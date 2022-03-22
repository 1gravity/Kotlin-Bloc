package org.orbitmvi.orbit.sample.posts.app.features.postdetails.viewmodel

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostDetail
import org.orbitmvi.orbit.sample.posts.domain.repositories.PostOverview

@Parcelize
data class PostDetailState(
    val postOverview: PostOverview,
    val loading: Boolean = false,
    val post: PostDetail? = null
) : Parcelable

