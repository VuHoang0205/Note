package com.muamuathu.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColorScheme(
    primary = Blue,
    primaryContainer = Blue,
    onPrimary = White,
    secondary = Green,
    secondaryContainer = Green,
    onSecondary = Black
)
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorPalette,
        content = content
    )
}