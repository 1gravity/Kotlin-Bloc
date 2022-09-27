@file:Suppress("MagicNumber")

package com.onegravity.bloc.util

import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val primaryColor = Color(0xFF283593)
val primaryColorLight = Color(0xFF5f5fc4)
val secondaryColor = Color(0xFF1b5e20)
val secondaryColorLight = Color(0xFF4c8c4a)

val darkColorPalette = darkColors(
    primary = primaryColor,
    primaryVariant = primaryColorLight,
    secondary = secondaryColor
)

val lightColorPalette = lightColors(
    primary = primaryColorLight,
    primaryVariant = primaryColorLight,
    secondary = secondaryColorLight
)
