package com.onegravity.bloc.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.onegravity.bloc.R
import com.onegravity.bloc.sample.posts.domain.repositories.Post

@Composable
internal fun Post(post: Post, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(start = 16.dp, end = 16.dp)) {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            item {
                Text(
                    text = post.title,
                    fontSize = 32.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = post.username,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = post.body,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Divider(modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 24.dp))

                Text(
                    text = stringResource(id = R.string.posts_compose_comments, post.comments.size),
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)
                )
            }
            items(post.comments) { comment ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = comment.name.uppercase(),
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = comment.email,
                        fontSize = 10.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = comment.body,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Divider(modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 24.dp))
            }
        }
    }

}
