package com.onegravity.bloc.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
internal fun Posts(
    component: PostsComponent,
    modifier: Modifier
) {
    val model by component.observeState()

    if (model.postsState.loading) {
        Box(modifier = modifier.background(Color.Transparent)) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        model.postsState.posts.mapBoth(
            { posts -> PostsList(posts, model.selectedPost, modifier) { component.onClicked(it) } },
            { error -> Error({ component.loadPosts() }, error) }
        )
    }
}
