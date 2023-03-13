package com.muamuathu.ui.modifier

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Stable
fun Modifier.mirror(horizontal: Boolean = false, vertical: Boolean = false): Modifier {
    return this.scale(
        scaleX = if (horizontal) -1f else 1f,
        scaleY = if (vertical) -1f else 1f
    )
}