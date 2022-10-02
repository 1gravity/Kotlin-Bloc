package com.onegravity.bloc.postsCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import co.touchlab.kermit.Logger
import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.compose.observeState
import com.onegravity.bloc.sample.posts.compose.PostsComponent

@Composable
@Suppress("FunctionNaming", "FunctionName")
internal fun PostPane(
    component: PostsComponent,
    modifier: Modifier
) {
    val state by component.observeState()

    when (state.postState.loadingId != null) {
        true -> Box(modifier = modifier.background(Color.Transparent)) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        else -> state.postState.post?.mapBoth(
            { post -> Post(post, modifier) },
            { error -> Error({ Logger.e("$error") }, error) }
        )
    }
}
