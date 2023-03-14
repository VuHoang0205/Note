package com.muamuathu.ui.icon

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun JcIcon(modifier: Modifier, iconSource: IconSource, tint: Color) {
    when (iconSource) {
        is IconSource.IconResourceSource -> Icon(
            modifier = modifier,
            painter = painterResource(id = iconSource.icon),
            contentDescription = "",
            tint = tint
        )
        is IconSource.Vector -> Icon(
            modifier = modifier,
            imageVector = iconSource.icon,
            contentDescription = "",
            tint = tint
        )
    }
}