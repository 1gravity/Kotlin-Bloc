package com.onegravity.bloc.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.github.michaelbull.result.mapBoth
import com.onegravity.bloc.Bloc
import com.onegravity.bloc.observeAsState
import com.onegravity.bloc.sample.posts.bloc.PostList
import com.onegravity.bloc.sample.posts.bloc.PostListState
import com.onegravity.bloc.sample.posts.domain.repositories.PostOverview
import com.onegravity.bloc.toLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Composable
fun PostsUi(bloc: Bloc<PostListState, PostList.Action, PostList.OpenPost, PostListState>, modifier: Modifier = Modifier) {
    val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    val model: PostListState by bloc.observeAsState()
    if (model.loading) {
        // todo loading message
    } else {
        model.overviews.mapBoth(
            { posts ->
                PostsList(posts, Modifier) { post ->
                    bloc.send(PostList.Action.Clicked(post))
                }
            },
            {
                // todo error message
            }
        )
    }
}

@Composable
fun PostsList(model: List<PostOverview>, modifier: Modifier = Modifier, onClicked: (post: PostOverview) -> Unit) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(model) { post ->
            val isSelected = false // todo article.id == model.selectedArticleId

            Text(
                text = post.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isSelected,
                        onClick = { onClicked(post) }
                    )
                    .run { if (isSelected) background(color = selectionColor()) else this }
                    .padding(16.dp)
            )

            Divider()
        }
    }
}

@Composable
private fun selectionColor(): Color =
    MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
