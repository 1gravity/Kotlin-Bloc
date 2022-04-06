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
import com.onegravity.bloc.observeState
import com.onegravity.bloc.sample.posts.compose.PostsComponent

@Composable
internal fun PostPane(
    component: PostsComponent,
    modifier: Modifier
) {
    val state by component.observeState()

    when (state.postState.loading != null) {
        true -> Box(modifier = modifier.background(Color.Transparent)) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        else -> state.postState.post?.mapBoth(
            { post -> Post(post, modifier) },
            { error -> Error({ component.loadPost() }, error) }
        )
    }
}
