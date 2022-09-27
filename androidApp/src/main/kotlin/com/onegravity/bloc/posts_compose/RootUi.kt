@file:Suppress("WildcardImport")

package com.onegravity.bloc.posts_compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.R
import com.onegravity.bloc.compose.observeState
import com.onegravity.bloc.sample.posts.compose.PostsComponent
import com.onegravity.bloc.sample.posts.compose.PostsRootState

private val MULTI_PANE_WIDTH_THRESHOLD = 700.dp

@Composable
@Suppress("FunctionNaming", "FunctionName")
fun RootUi(component: PostsComponent) {
    val state: PostsRootState by component.observeState()

    BoxWithConstraints(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val isMultiPane = this@BoxWithConstraints.maxWidth >= MULTI_PANE_WIDTH_THRESHOLD
        Scaffold(
            topBar = { ToolBar(component, isMultiPane) }
        ) { padding ->
            val showDetail = state.postIsLoading() || state.postIsLoaded()
            val modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(padding)
            when {
                isMultiPane && showDetail ->
                    Row(modifier) {
                        PostsPane(component,
                            Modifier
                                .fillMaxWidth(0.33f)
                                .fillMaxHeight())
                        PostPane(component,
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight())
                    }
                showDetail -> PostPane(component, modifier)
                else -> PostsPane(component, modifier)
            }
        }
    }
}

@Composable
@Suppress("FunctionNaming", "FunctionName")
private fun ToolBar(component: PostsComponent, isMultiPane: Boolean) {
    val state by component.observeState()
    val showDetail = state.postIsLoading() || state.postIsLoaded()

    val defaultTitle = stringResource(R.string.posts_compose_title)
    val title = when {
        isMultiPane -> defaultTitle
        state.postIsLoaded() -> state.postState.post?.mapBoth({ it.username }, { defaultTitle })
        state.postState.loadingId != null -> stringResource(R.string.posts_compose_loading)
        else -> defaultTitle
    } ?: defaultTitle

    if (isMultiPane || !showDetail) {
        TopAppBar(title = { Text(text = title) })
    } else {
        BackHandler(onBack = { component.onClosed() })
        TopAppBar(
            title = { Text(text = title) },
            navigationIcon = {
                IconButton(onClick = { component.onClosed() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }
}