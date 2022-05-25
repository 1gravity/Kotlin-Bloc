package com.onegravity.bloc.posts_compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.compose.observeState
import com.onegravity.bloc.sample.posts.compose.PostsComponent
import com.onegravity.bloc.utils.logger

@Composable
internal fun PostsPane(
    component: PostsComponent,
    modifier: Modifier
) {
    val state by component.observeState()

    when (state.postsState.loading) {
        true -> Box(modifier = modifier.background(Color.Transparent)) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        else -> state.postsState.posts.mapBoth(
            { posts ->
                Posts(posts, state.selectedPost(), modifier) { post ->
                    component.onSelected(post)
                }
            },
            { error -> Error({ logger.e("$error") }, error) }
        )
    }
}
