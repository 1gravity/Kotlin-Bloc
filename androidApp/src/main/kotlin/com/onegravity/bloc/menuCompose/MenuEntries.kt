@file:Suppress("WildcardImport")

package com.onegravity.bloc.menuCompose

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onegravity.bloc.compose.observeSideEffects
import com.onegravity.bloc.compose.observeState
import com.onegravity.bloc.compose.previewBlocContext
import com.onegravity.bloc.sample.MainMenuCompose
import com.onegravity.bloc.sample.MenuBloc

@Composable
@Suppress("FunctionNaming", "FunctionName")
internal fun MenuEntries(bloc: MenuBloc, modifier: Modifier) {
    val state by bloc.observeState()
    val sideEffect by bloc.observeSideEffects()

    // in a real application we would use the navigation component
    // in a real application we would also use just one Activity so "navigation" would be obsolete anyway
    // it does however show how side effects can be used in a Composable
    sideEffect?.let { menuEntry ->
        val context = LocalContext.current
        val intent = Intent(context, menuItem2Activity[menuEntry])
        context.startActivity(intent)
    }

    LazyColumn(modifier = modifier.padding(top = 24.dp)) {
        items(state.allMenus) { menuItem ->
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .defaultMinSize(200.dp)
                        .width(IntrinsicSize.Min),
                    onClick = { bloc.send(menuItem) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    content = {
                        val text =
                            menuItem2Text[menuItem]?.let { stringResource(it) } ?: "Text not found"
                        Text(text = text, textAlign = TextAlign.Center, color = Color.White)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
@Suppress("FunctionNaming", "FunctionName")
fun MenuEntriesPreview() {
    val bloc = MainMenuCompose.bloc(previewBlocContext())
    MenuEntries(bloc,
        Modifier
            .fillMaxWidth()
            .fillMaxHeight())
}
