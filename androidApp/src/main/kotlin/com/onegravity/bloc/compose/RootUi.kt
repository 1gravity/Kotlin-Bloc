package com.onegravity.bloc.compose

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.R
import com.onegravity.bloc.observeState
import com.onegravity.bloc.sample.posts.compose.PostsComponent

// todo implement multipane

@Composable
fun RootUi(component: PostsComponent) {
    val state by component.observeState()
    val showDetail = state.selectedPost != null
    Scaffold(
        topBar = { ToolBar(component) }
    ) {
        if (showDetail) {
            PostScreen(component)
        } else {
            Posts(component)
        }
    }
}

@Composable
private fun ToolBar(component: PostsComponent) {
    val state by component.observeState()
    val showDetail = state.selectedPost != null
    val defaultTitle = stringResource(R.string.posts_compose_title)
    val title = when (showDetail) {
        true -> state.postState.post?.mapBoth({ it.username }, { it.message })
        else -> defaultTitle
    } ?: defaultTitle
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            if (showDetail) IconButton(onClick = { component.onClosed() }) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        },
    )
}
