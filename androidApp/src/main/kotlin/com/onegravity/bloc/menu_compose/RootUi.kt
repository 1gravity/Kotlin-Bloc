package com.onegravity.bloc.menu_compose

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.start
import com.onegravity.bloc.context.DefaultBlocContext
import com.onegravity.bloc.sample.MainMenuCompose
import com.onegravity.bloc.sample.MenuBloc

@Composable
fun RootUi(bloc: MenuBloc) {
    BoxWithConstraints(
        Modifier.fillMaxWidth().fillMaxHeight()) {
        Scaffold(
//            topBar = { TopAppBar(
//                title = { Text(text = stringResource(R.string.posts_compose_title)) }
//            ) }
        ) {
            MenuEntries(bloc, Modifier.fillMaxWidth().fillMaxHeight()) }
    }
}

@Preview
@Composable
fun RootUiPreview() {
    // todo create a function to create a default context for previews (lifecycle tied to composable)
    //      put this into the Android extensions because that we don't need it for other platforms
    val lifecycleRegistry = LifecycleRegistry()
    DisposableEffect(key1 = lifecycleRegistry) {
        lifecycleRegistry.start()
        onDispose { lifecycleRegistry.destroy() }
    }
    val bloc = MainMenuCompose.bloc(DefaultBlocContext(lifecycleRegistry, null, null, null))
    RootUi(bloc)
}
