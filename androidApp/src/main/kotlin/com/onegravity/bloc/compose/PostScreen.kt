package com.onegravity.bloc.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.observeState
import com.onegravity.bloc.sample.posts.compose.PostsComponent

@Composable
internal fun PostScreen(
    component: PostsComponent,
    modifier: Modifier
) {
    val model by component.observeState()

    when (model.postState.loading) {
        true -> Box(modifier = modifier.background(Color.Transparent)) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        else -> model.postState.post?.mapBoth(
            { post -> Post(post, modifier) },
            { error -> Error({ component.loadPost() }, error) }
        )
    }
}
