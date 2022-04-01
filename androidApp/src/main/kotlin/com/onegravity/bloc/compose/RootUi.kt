package com.onegravity.bloc.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.R
import com.onegravity.bloc.observeState
import com.onegravity.bloc.sample.posts.compose.PostsComponent

private val MULTI_PANE_WIDTH_THRESHOLD = 700.dp

@Composable
fun RootUi(component: PostsComponent) {
    val state by component.observeState()
    BoxWithConstraints(
        Modifier.fillMaxWidth().fillMaxHeight()) {
        val isMultiPane = this@BoxWithConstraints.maxWidth >= MULTI_PANE_WIDTH_THRESHOLD
        Scaffold(
            topBar = { ToolBar(component, isMultiPane) }
        ) {
            val showDetail = state.selectedPost != null

            when {
                isMultiPane && showDetail ->
                    Row(
                        Modifier.fillMaxWidth().fillMaxHeight()) {
                        PostsPane(component, Modifier.fillMaxWidth(0.33f).fillMaxHeight())
                        PostPane(component, Modifier.fillMaxWidth().fillMaxHeight())
                    }
                showDetail -> PostPane(component, Modifier.fillMaxWidth().fillMaxHeight())
                else -> PostsPane(component, Modifier.fillMaxWidth().fillMaxHeight())
            }
        }
    }
}

@Composable
private fun ToolBar(component: PostsComponent, isMultiPane: Boolean) {
    val state by component.observeState()
    val showDetail = state.selectedPost != null

    val title = when (showDetail) {
        true -> state.postState.post?.mapBoth({ it.username }, { it.message })
        else -> null
    } ?: stringResource(R.string.posts_compose_title)

    if (isMultiPane || !showDetail) {
        TopAppBar(title = { Text(text = title) },)
    } else {
        BackHandler(onBack = { component.onClosed() })
        TopAppBar(
            title = { Text(text = title) },
            navigationIcon = {
                IconButton(onClick = { component.onClosed() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
        )
    }
}