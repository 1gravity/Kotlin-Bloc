package com.onegravity.bloc.compose

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.onegravity.bloc.R
import com.onegravity.bloc.observeState
import com.onegravity.bloc.sample.posts.compose.PostsComponent

// todo convert to multipane

@Composable
fun RootUi(component: PostsComponent) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.posts_compose_title))
                }
            )
        }
    ) {
        val state by component.observeState()
        if (state.selectedPost != null) {
            PostScreen(component)
        } else {
            Posts(component)
        }
    }
}
