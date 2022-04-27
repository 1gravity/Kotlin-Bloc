package com.onegravity.bloc.menu_compose

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.onegravity.bloc.compose.previewBlocContext
import com.onegravity.bloc.sample.MainMenuCompose
import com.onegravity.bloc.sample.MenuBloc

@Composable
fun RootUi(bloc: MenuBloc) {
    BoxWithConstraints(
        Modifier.fillMaxWidth().fillMaxHeight()) {
        MenuEntries(bloc, Modifier.fillMaxWidth().fillMaxHeight())
    }
}

@Preview
@Composable
fun RootUiPreview() {
    val bloc = MainMenuCompose.bloc(previewBlocContext())
    RootUi(bloc)
}
