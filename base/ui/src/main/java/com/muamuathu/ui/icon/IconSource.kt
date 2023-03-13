package com.muamuathu.ui.icon

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface IconSource {
    data class Vector(val icon: ImageVector) : IconSource
    data class IconResourceSource(@DrawableRes val icon: Int) : IconSource
}