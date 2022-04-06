package com.onegravity.bloc.posts_compose

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.onegravity.bloc.sample.posts.domain.repositories.Post

@Composable
internal fun Posts(
    posts: List<Post>,
    selected: Int?,
    modifier: Modifier = Modifier,
    onClicked: (post: Post) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(posts) { post ->
            PostItem(post, post.id == selected, onClicked)
            Divider()
        }
    }
}

