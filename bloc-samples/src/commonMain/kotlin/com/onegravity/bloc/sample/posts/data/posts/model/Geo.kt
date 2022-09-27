package com.onegravity.bloc.sample.posts.data.posts.model

import kotlinx.serialization.Serializable

@Serializable
data class Geo(
    val lat: String,
    val lng: String
)
