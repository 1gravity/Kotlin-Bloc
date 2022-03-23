package com.onegravity.bloc.sample.posts.data.posts.model

import kotlinx.serialization.Serializable

@Serializable
data class Company(
    val bs: String,
    val catchPhrase: String,
    val name: String
)