@file:Suppress("WildcardImport")

package com.onegravity.bloc.postsCompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onegravity.bloc.sample.posts.domain.repositories.Post
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
@Suppress("FunctionNaming", "FunctionName")
internal fun PostItem(
    post: Post,
    isSelected: Boolean,
    onClicked: (post: Post) -> Unit
) {
    @Composable
    fun selectionColor(): Color =
        MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = { onClicked(post) }
            )
            .run { if (isSelected) background(color = selectionColor()) else this }
            .padding(16.dp)

    ) {
        Box(
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .padding(16.dp)
        ) {
            GlideImage(
                imageModel = post.avatarUrl,
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                ),
                loading = {
                    Box(modifier = Modifier.matchParentSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = post.username,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = post.title,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )
        }
    }
}

