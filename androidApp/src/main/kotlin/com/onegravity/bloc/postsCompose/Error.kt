package com.onegravity.bloc.postsCompose

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.onegravity.bloc.R

@Composable
@Suppress("FunctionNaming", "FunctionName")
fun Error(retry: () -> Unit, error: Throwable) {
    Snackbar(
        action = { Button(retry) { Text(stringResource(id = R.string.posts_compose_retry)) } },
        modifier = Modifier.padding(8.dp)
    ) {
        Text(stringResource(id = R.string.posts_compose_error, error.message ?: ""))
    }
}
